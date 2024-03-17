package com.papersaccul.paperffeditor.util;

import com.papersaccul.paperffeditor.model.VideoSettings;

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
        
        // Codec
        if (settings.getCodec() != null && !settings.getCodec().isEmpty()) {
            command.append(" -c:v ").append(settings.getCodec());
        }
        
        // Bitrate
        if (settings.getBitrate() > 0) {
            command.append(" -b:v ").append(settings.getBitrate()).append("k");
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
        if (settings.getResolution() != null && !settings.getResolution().isEmpty()) {
            command.append(" -s ").append(settings.getResolution());
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
}
