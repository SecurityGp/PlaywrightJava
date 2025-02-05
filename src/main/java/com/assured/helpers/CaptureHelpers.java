package com.assured.helpers;

import com.assured.constants.FrameworkConstants;
import com.assured.utils.LogUtils;
import com.microsoft.playwright.Page;
import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CaptureHelpers {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");

    /**
     * Captures a screenshot using Playwright's Page API.
     * The screenshot file is saved in the folder specified in FrameworkConstants.EXPORT_CAPTURE_PATH.
     *
     * @param page       The Playwright Page instance.
     * @param screenName The name to include in the screenshot filename.
     */
    public static void captureScreenshot(Page page, String screenName) {
        try {
            String path = SystemHelpers.getCurrentDir() + FrameworkConstants.EXPORT_CAPTURE_PATH;
            File folder = new File(path);
            if (!folder.exists()) {
                LogUtils.info("No Folder: " + path);
                folder.mkdir();
                LogUtils.info("Folder created: " + folder.getAbsolutePath());
            }

            String fileName = screenName + "_" + dateFormat.format(new Date()) + ".png";
            String filePath = path + File.separator + fileName;

            // Use Playwright's screenshot API
            page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(filePath)));

            LogUtils.info("Screenshot taken: " + fileName);
            LogUtils.info("Screenshot URL: " + page.url());
        } catch (Exception e) {
            LogUtils.error("Exception while taking screenshot: " + e.getMessage(), e);
        }
    }

    /**
     * Captures a full-screen screenshot using AWT Robot and returns the File reference.
     *
     * @param screenshotName The base name for the screenshot file.
     * @return The File containing the screenshot.
     */
    public static File getScreenshotFile(String screenshotName) {
        Rectangle allScreenBounds = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        String dateName = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss.SSS").format(new Date());
        BufferedImage image;
        try {
            image = new Robot().createScreenCapture(allScreenBounds);
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }

        String path = SystemHelpers.getCurrentDir() + FrameworkConstants.EXTENT_REPORT_FOLDER + File.separator + "images";
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdir();
            LogUtils.info("Folder created: " + folder.getAbsolutePath());
        }

        String filePath = path + File.separator + screenshotName + dateName + ".png";
        File file = new File(filePath);
        try {
            ImageIO.write(image, "PNG", file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return file;
    }

    /**
     * Captures a full-screen screenshot using AWT Robot and returns the relative path.
     *
     * @param screenshotName The base name for the screenshot file.
     * @return The relative path to the screenshot.
     */
    public static String getScreenshotRelativePath(String screenshotName) {
        Rectangle allScreenBounds = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        String dateName = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss.SSS").format(new Date());
        BufferedImage image;
        try {
            image = new Robot().createScreenCapture(allScreenBounds);
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }

        String path = SystemHelpers.getCurrentDir() + FrameworkConstants.EXTENT_REPORT_FOLDER + File.separator + "images";
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdir();
            LogUtils.info("Folder created: " + folder.getAbsolutePath());
        }

        String filePath = path + File.separator + screenshotName + dateName + ".png";
        File file = new File(filePath);
        try {
            ImageIO.write(image, "PNG", file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return FrameworkConstants.EXTENT_REPORT_FOLDER + File.separator + "images" + File.separator + screenshotName + dateName + ".png";
    }

    /**
     * Captures a full-screen screenshot using AWT Robot and returns the absolute file path.
     *
     * @param screenshotName The base name for the screenshot file.
     * @return The absolute path to the screenshot file.
     */
    public static String getScreenshotAbsolutePath(String screenshotName) {
        Rectangle allScreenBounds = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        String dateName = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss.SSS").format(new Date());
        BufferedImage image;
        try {
            image = new Robot().createScreenCapture(allScreenBounds);
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }

        String path = SystemHelpers.getCurrentDir() + FrameworkConstants.EXTENT_REPORT_FOLDER + File.separator + "images";
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdir();
            LogUtils.info("Folder created: " + folder.getAbsolutePath());
        }

        String filePath = path + File.separator + screenshotName + dateName + ".png";
        File file = new File(filePath);
        try {
            ImageIO.write(image, "PNG", file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return filePath;
    }
}
