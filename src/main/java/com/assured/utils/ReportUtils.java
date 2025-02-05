package com.assured.utils;

import com.assured.exceptions.FrameworkException;
import com.assured.exceptions.InvalidPathForExtentReportFileException;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static com.assured.constants.FrameworkConstants.*;

public final class ReportUtils {

    // Private constructor to prevent instantiation.
    private ReportUtils() {
    }

    /**
     * Creates the full path for the extent report file.
     * <p>
     * If OVERRIDE_REPORTS is set to NO, the report file name will include the current date.
     * Otherwise, a static report file name is used.
     * </p>
     *
     * @return The full file path for the extent report.
     */
    public static String createExtentReportPath() {
        String reportPath;
        if (OVERRIDE_REPORTS.trim().equalsIgnoreCase(NO)) {
            // For non-overriding, include the current date in the file name.
            reportPath = EXTENT_REPORT_FOLDER_PATH + File.separator + DateUtils.getCurrentDate() + "_" + EXTENT_REPORT_FILE_NAME;
        } else {
            // For overriding, use a static file name.
            reportPath = EXTENT_REPORT_FOLDER_PATH + File.separator + EXTENT_REPORT_FILE_NAME;
        }
        System.out.println("Created report path: " + reportPath);
        return reportPath;
    }

    /**
     * Opens the generated extent report file in the default browser if OPEN_REPORTS_AFTER_EXECUTION is set to YES.
     *
     * @param linkReport The full file path to the extent report.
     * @throws InvalidPathForExtentReportFileException if the file is not found.
     * @throws FrameworkException                        if an IOException occurs while opening the file.
     */
    public static void openReports(String linkReport) {
        if (OPEN_REPORTS_AFTER_EXECUTION.trim().equalsIgnoreCase(YES)) {
            try {
                File reportFile = new File(linkReport);
                if (!reportFile.exists()) {
                    throw new FileNotFoundException("Report file not found: " + linkReport);
                }
                Desktop.getDesktop().browse(reportFile.toURI());
            } catch (FileNotFoundException e) {
                throw new InvalidPathForExtentReportFileException(
                        "Extent Report file you are trying to reach is not found", e.fillInStackTrace());
            } catch (IOException e) {
                throw new FrameworkException(
                        "Extent Report file you are trying to reach got IOException while reading the file", e.fillInStackTrace());
            }
        }
    }
}
