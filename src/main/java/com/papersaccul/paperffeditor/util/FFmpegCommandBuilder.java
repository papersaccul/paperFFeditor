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
        StringBuilder command = new StringBuilder("ffmpeg -i ");
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
        if (settings.getVideoBitrate() > 0) {
            command.append(" -b:v ").append(settings.getVideoBitrate()).append("k");
        }
        // Audio Bitrate
        if (settings.getAudioBitrate() > 0) {
            command.append(" -b:a ").append(settings.getAudioBitrate()).append("k");
        }
        
        // Volume
        if (settings.getVolume() != 100) { // Assuming 100 is the default volume percentage
            command.append(" -filter:a \"volume=").append(settings.getVolume()).append("%\"");
        }
        
        // Frame rate
        if (settings.getFrameRate() > 0) {
            command.append(" -r ").append(settings.getFrameRate());
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
     * Get specific video information based on the requested detail
     * @param filePath path to media
     * @param detail the detail to retrieve (e.g., "audioCodec", "videoCodec", "bitrate", "resolution", "frameRate", "audioChannels")
     * @return the requested detail information as a string
     */
    public static String getVideoInfo(String filePath, String detail) {
        String command = "ffmpeg -i " + quote(filePath);
        Map<String, String> videoDetails = new HashMap<>();
        try {
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("Stream #0:0")) { // Video stream
                    videoDetails.put("videoCodec", cleanCodecName(extractDetail(line, "Video: ")));
                    // drawingMethodDetail doesn't work correctly, use only for logic
                    String drawingMethodDetail = extractDetail(line, "Video: ", "(").trim();
                    videoDetails.put("drawingMethod", drawingMethodDetail);
                    String resolutionDetail = extractDetail(line, "),", "[SAR");
                    if (resolutionDetail.isEmpty()) {
                        resolutionDetail = extractDetail(line, drawingMethodDetail + ")", "[SAR");
                    }
                    String[] resolutionParts = resolutionDetail.split("x");
                    if (resolutionParts.length == 2) {
       
                        String width = resolutionParts[0].substring(resolutionParts[0].lastIndexOf(" ")).trim();
                        videoDetails.put("width", width);
                        videoDetails.put("height", resolutionParts[1].split(" ")[0].trim()); 
                    }
                    videoDetails.put("frameRate", extractDetail(line, "fps"));
                } else if (line.contains("Stream #0:1")) { // Audio stream
                    videoDetails.put("audioCodec", cleanCodecName(extractDetail(line, "Audio: ")));
                    videoDetails.put("audioChannels", extractDetail(line, "Audio: ", ","));
                    // Parse audio bitrate
                    String audioBitrateDetail = extractDetail(line, "Audio: ", ",");
                    String[] audioBitrateParts = audioBitrateDetail.split(",");
                    for (String part : audioBitrateParts) {
                        if (part.trim().endsWith("kb/s")) {
                            videoDetails.put("audioBitrate", part.trim());
                            break;
                        }
                    }
                } else if (line.contains("bitrate:")) {
                    videoDetails.put("videoBitrate", extractDetail(line, "bitrate: "));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return videoDetails.getOrDefault(detail, "Detail not found");
    }
    
    /**
     * Cleans the codec name by removing any trailing commas.
     * 
     * @param codecName the codec name to clean
     * @return the cleaned codec name
     */
    private static String cleanCodecName(String codecName) {
        return codecName.endsWith(",") ? codecName.substring(0, codecName.length() - 1) : codecName;
    }
    
    /**
     * Extracts specific detail from a line of ffmpeg output
     * 
     * @param line the line of text to parse
     * @param startAfter the text immediately before the detail to extract
     * @return the extracted detail as a string
     */
    private static String extractDetail(String line, String startAfter) {
        int startIndex = line.indexOf(startAfter) + startAfter.length();
        int endIndex = line.indexOf(" ", startIndex);
        return line.substring(startIndex, endIndex == -1 ? line.length() : endIndex);
    }
    
    /**
     * Overloaded method to extract resolution which has a unique format
     * 
     * @param line the line of text to parse
     * @param startAfter the text immediately before the detail to extract
     * @param delimiter the delimiter used in the detail (e.g., "x" in resolution)
     * @return the extracted detail as a string
     */
    private static String extractDetail(String line, String startAfter, String delimiter) {
        int startIndex = line.indexOf(startAfter) + startAfter.length();
        int endIndex = line.indexOf(delimiter, startIndex) + delimiter.length();
        endIndex += line.substring(endIndex).indexOf(" ");
        return line.substring(startIndex, endIndex == -1 ? line.length() : endIndex);
    }
}

