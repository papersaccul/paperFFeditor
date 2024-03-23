package com.papersaccul.paperffeditor.util;

import com.papersaccul.paperffeditor.model.VideoSettings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;


/**
 * FFmpegCommandBuilder class is responsible for building FFmpeg command strings
 * based on the provided video settings.
 */
public class FFmpegCommandBuilder {
    /**
     * Builds the FFmpeg command string based on the provided VideoSettings.
     * 
     * @param inputFilePath the path to the input file
     * @param outputFilePath the path to the output file
     * @param settings the video settings
     * @return the FFmpeg command as a String
     */
    public static String buildCommand(String inputFilePath, String outputFilePath, VideoSettings settings) {
        String ffmpegPath = settings.getFfmpegPath();
        StringBuilder command = new StringBuilder(ffmpegPath + " -i ");

        
        command.append(quote(inputFilePath));
        

// Video Codec
        if (settings.getVideoCodec() != null && !settings.getVideoCodec().isEmpty()) {
            command.append(" -c:v ").append(settings.getVideoCodec());
        }

// Audio Codec
        if (settings.getAudioCodec() != null && !settings.getAudioCodec().isEmpty()) {
            command.append(" -c:a ").append(settings.getAudioCodec());
        }
        
// Video Bitrate
        if (settings.getVideoBitrate() != null && !settings.getVideoBitrate().isEmpty()) {
            try {
                int videoBitrate = Integer.parseInt(settings.getVideoBitrate());
                if (videoBitrate > 0) {
                    command.append(" -b:v ").append(videoBitrate).append("k");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error parsing video bitrate: " + e.getMessage());
            }
        }

// Audio Bitrate
        if (settings.getAudioBitrate() != null && !settings.getAudioBitrate().isEmpty()) {
            try {
                int audioBitrate = Integer.parseInt(settings.getAudioBitrate());
                if (audioBitrate > 0) {
                    command.append(" -b:a ").append(audioBitrate).append("k");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error parsing audio bitrate: " + e.getMessage());
            }
        }
        
// Volume
        if (settings.getVolume() != 100) { // Assuming 100 is the default volume percentage
            command.append(" -filter:a \"volume=").append(settings.getVolume()).append("%\"");
        }
        
// Frame rate
        if (settings.getFrameRate() != null && !settings.getFrameRate().isEmpty()) {
            try {
                int frameRate = Integer.parseInt(settings.getFrameRate());
                if (frameRate > 0) {
                    command.append(" -r ").append(frameRate);
                }
            } catch (NumberFormatException e) {
                System.out.println("Error parsing frame rate: " + e.getMessage());
            }
        }
        
// Resolution
        if (settings.getVideoWidth() != null && !settings.getVideoWidth().isEmpty() && settings.getVideoHeight() != null && !settings.getVideoHeight().isEmpty()) {
            command.append(" -s ").append(settings.getVideoWidth()).append("x").append(settings.getVideoHeight());
        }
        
// Audio Channels
        if (settings.getAudioChannels() != null && !settings.getAudioChannels().isEmpty()) {
            command.append(" -ac ").append(convertAudioChannelsToNumber(settings.getAudioChannels()));
        }
        
        command.append(" ").append(quote(outputFilePath));
        
        return command.toString();
    }
    
    /**
     * Wraps the given file path in quotes to handle spaces.
     * 
     * @param filePath the file path to be wrapped
     * @return the quoted file path
     */
    private static String quote(String filePath) {
        return "\"" + filePath + "\"";
    }
    
    /**
     * Converts audio channel descriptions to their numerical equivalents.
     * 
     * @param audioChannels the audio channel description
     * @return the numerical equivalent of the audio channels
     */
    private static String convertAudioChannelsToNumber(String audioChannels) {
        switch (audioChannels.toLowerCase()) {
            case "mono":
                return "1";
            case "stereo":
                return "2";
            default:
                return "2"; // Default to stereo if unknown
        }
    }
    
    /**
     * Parses video information and returns a map with all the details.
     * 
     * @param filePath the path to the video file
     * @param settings the video settings
     * @return a map containing all parsed video details
     */
    public static Map<String, String> parseVideoInfo(String filePath, VideoSettings settings) {

        String ffmpegPath = settings.getFfmpegPath();
        String command;

        if (ffmpegPath != null) {
            command = ffmpegPath + " -i " + quote(filePath);
        } else {
            command = "ffmpeg -i " + quote(filePath);
        }

        Map<String, String> details = new HashMap<>();
        // String videoBitrate = "";
        // String audioBitrate = "";
        try {
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line;
            while ((line = reader.readLine()) != null) {
        // Video
                if (line.contains("Stream") && line.contains("Video:")) {
                    
                    details.put("videoCodec", cleanCodecName(extractDetail(line, "Video: ")));
                    String drawingMethodDetail = extractDetail(line, "Video: ", "(").trim();
                    details.put("drawingMethod", drawingMethodDetail);

                    String resolutionDetail = extractDetail(line, "),", "[SAR");
                    if (resolutionDetail.isEmpty()) {
                        resolutionDetail = extractDetail(line, drawingMethodDetail + ")", "[SAR");
                    }
                    String[] resolutionParts = resolutionDetail.split("x");
                    if (resolutionParts.length == 2) {
                        String width = resolutionParts[0].substring(resolutionParts[0].lastIndexOf(" ")).trim();
                        details.put("videoWidth", width);
                        details.put("videoHeight", resolutionParts[1].split(" ")[0].trim()); 
                    }
                    details.put("frameRate", extractFrameRate(line));
                    details.put("videoBitrate", extractBitrate(line));
                    
        // Audio
                } else if (line.contains("Stream") && line.contains("Audio:")) {
                    details.put("audioCodec", cleanCodecName(extractDetail(line, "Audio: ")));
                    details.put("audioChannels", extractDetail(line, "Audio: ", ","));
                    details.put("audioBitrate", extractAudioBitrate(line));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return details;
    }
    
    private static String extractFrameRate(String line) {
        String[] parts = line.split(",");
        for (String part : parts) {
            if (part.trim().contains("fps")) {
                return part.trim().split(" ")[0];
            }
        }
        return "Detail not found";
    }
    
    private static String cleanCodecName(String codecName) {
        return codecName.endsWith(",") ? codecName.substring(0, codecName.length() - 1) : codecName;
    }
    
    private static String extractDetail(String line, String startAfter) {
        int startIndex = line.indexOf(startAfter) + startAfter.length();
        int endIndex = line.indexOf(" ", startIndex);
        return line.substring(startIndex, endIndex == -1 ? line.length() : endIndex);
    }
    
    private static String extractDetail(String line, String startAfter, String delimiter) {
        int startIndex = line.indexOf(startAfter) + startAfter.length();
        int endIndex = line.indexOf(delimiter, startIndex) + delimiter.length();
        endIndex += line.substring(endIndex).indexOf(" ");
        return line.substring(startIndex, endIndex == -1 ? line.length() : endIndex);
    }

    private static String extractAudioBitrate(String line) {
        String[] parts = line.split(",");
        for (String part : parts) {
            if (part.trim().contains("kb/s")) {
                return part.trim().split(" ")[0];
            }
        }
        return "Detail not found";
    }

    private static String extractBitrate(String line) {
        String bitrate = "Detail not found";
        String[] parts = line.split(",");
        for (String part : parts) {
            if (part.trim().contains("kb/s")) {
                bitrate = part.trim().split(" ")[0];
                break;
            }
        }
        return bitrate;
    }
}
