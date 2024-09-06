package com.depromeet.spreadsheet;

import static com.depromeet.type.withdrawal.WithdrawalReasonErrorType.FAILED_TO_INSERT_DATA_TO_SPREADSHEET;

import com.depromeet.config.SpreadSheetProperties;
import com.depromeet.exception.InternalServerException;
import com.depromeet.image.domain.Image;
import com.depromeet.report.port.in.command.CreateReportCommand;
import com.depromeet.report.port.out.persistence.ReportPersistencePort;
import com.depromeet.withdrawal.domain.ReasonType;
import com.depromeet.withdrawal.port.out.persistence.WithdrawalReasonPort;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GoogleSheetManager implements WithdrawalReasonPort, ReportPersistencePort {
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS);

    private final SpreadSheetProperties spreadSheetProperties;

    @Value("${cloud-front.domain}")
    private String domain;

    @Override
    public void writeToSheet(ReasonType reasonType, String feedback) {
        try {
            Sheets sheet = getSheetService(spreadSheetProperties.applicationName());
            List<List<Object>> data = getData(reasonType, feedback);
            ValueRange valueRange = new ValueRange().setValues(data);

            AppendValuesResponse appendResult =
                    sheet.spreadsheets()
                            .values()
                            .append(
                                    spreadSheetProperties.sheetId(),
                                    spreadSheetProperties.range(),
                                    valueRange)
                            .setValueInputOption("RAW")
                            .setInsertDataOption("INSERT_ROWS")
                            .setIncludeValuesInResponse(true)
                            .execute();
        } catch (Exception e) {
            log.error("Error writing to sheet", e);
            throw new InternalServerException(FAILED_TO_INSERT_DATA_TO_SPREADSHEET);
        }
    }

    private Sheets getSheetService(String sheetApplicationName)
            throws IOException, GeneralSecurityException {
        GoogleCredentials googleCredentials =
                GoogleCredentials.fromStream(
                                new FileInputStream(spreadSheetProperties.credentialsFilePath()))
                        .createScoped(SCOPES);
        return new Sheets.Builder(
                        GoogleNetHttpTransport.newTrustedTransport(),
                        JSON_FACTORY,
                        new HttpCredentialsAdapter(googleCredentials))
                .setApplicationName(sheetApplicationName)
                .build();
    }

    private List<List<Object>> getData(ReasonType reasonType, String feedback) {
        String date = getDateTimeNow();
        feedback = feedback != null ? feedback : "";

        List<List<Object>> data = new ArrayList<>();
        data.add(List.of(reasonType.getCode(), reasonType.getReason(), feedback, date));
        return data;
    }

    @Override
    public void writeReportToSheet(CreateReportCommand command) {
        try {
            Sheets sheet = getSheetService(spreadSheetProperties.reportApplicationName());
            List<List<Object>> data = getReportData(command);
            ValueRange valueRange = new ValueRange().setValues(data);

            AppendValuesResponse appendResult =
                    sheet.spreadsheets()
                            .values()
                            .append(
                                    spreadSheetProperties.reportSheetId(),
                                    spreadSheetProperties.reportRange(),
                                    valueRange)
                            .setValueInputOption("RAW")
                            .setInsertDataOption("INSERT_ROWS")
                            .setIncludeValuesInResponse(true)
                            .execute();
        } catch (Exception e) {
            log.error("Error writing to sheet", e);
            throw new InternalServerException(FAILED_TO_INSERT_DATA_TO_SPREADSHEET);
        }
    }

    private List<List<Object>> getReportData(CreateReportCommand command) {
        String date = getDateTimeNow();
        String diary = command.reportMemory().diary() == null ? "" : command.reportMemory().diary();

        List<List<Object>> data = new ArrayList<>();
        data.add(
                List.of(
                        command.member().id(),
                        command.member().nickname(),
                        command.reportMemory().member().id(),
                        command.reportMemory().member().nickname(),
                        command.reportMemory().id(),
                        getImagesUrl(command.images()),
                        diary,
                        command.reasonCode().name(),
                        command.reasonCode().getValue(),
                        date));
        return data;
    }

    private static String getDateTimeNow() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    private String getImagesUrl(List<Image> images) {
        return images.isEmpty()
                ? ""
                : images.stream()
                        .map(image -> domain + "/" + image.getImageName())
                        .toList()
                        .toString();
    }
}
