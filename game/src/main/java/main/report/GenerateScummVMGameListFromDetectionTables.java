package main.report;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class GenerateScummVMGameListFromDetectionTables {
    private static final Logger LOGGER = LogManager.getLogger(GenerateScummVMGameListFromDetectionTables.class);
    private static final String SEPERATOR = "\t";

    public static void main(String[] args) throws Exception {

        Path writeFilePath = Paths.get("/Users/vangos/Development/git/lazygalaxy-java/game/target/scummvm_id_map.txt");
        Files.writeString(writeFilePath, "ID" + SEPERATOR + "Engine" + SEPERATOR + "Name\n", StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING);
        Set<String> writeSet = new TreeSet<String>();

        // scumm engine
        Path readFilePath = Paths.get(
                "/Users/vangos/Development/git/lazygalaxy-java/game/src/main/resources/scummvm/detection_tables.h/scumm_detection_tables.h");
        List<String> scummLines = Files.readAllLines(readFilePath);
        for (String scummLine : scummLines) {
            scummLine = scummLine.trim();
            if (StringUtils.contains(scummLine, "{") && StringUtils.contains(scummLine, "}")) {
                String[] tokens = StringUtils.split(scummLine, "{},");
                for (int i = 0; i < tokens.length; i++) {
                    tokens[i] = tokens[i].trim().replaceAll("\"", "");
                }
                if (tokens.length == 2 && tokens[1].length() > 3) {
                    writeSet.add(tokens[0].toLowerCase() + SEPERATOR + "scumm" + SEPERATOR + tokens[1] + "\n");
                }
            }

        }

        // agi engine
        readFilePath = Paths.get(
                "/Users/vangos/Development/git/lazygalaxy-java/game/src/main/resources/scummvm/detection_tables.h/agi_detection_tables.h");
        scummLines = Files.readAllLines(readFilePath);
        for (int l = 0; l < scummLines.size(); l++) {
            String scummLine = scummLines.get(l).trim();
            if (StringUtils.startsWith(scummLine, "GAME")) {
                String[] tokens = StringUtils.split(scummLine, "(),");
                for (int i = 0; i < tokens.length; i++) {
                    tokens[i] = tokens[i].trim().replaceAll("\"", "");
                }
                if (tokens.length > 2) {
                    String dirtyName = null;
                    for (int j = l; ; j--) {
                        dirtyName = scummLines.get(j).trim();
                        if (StringUtils.isBlank(dirtyName)) {
                            dirtyName = scummLines.get(j + 1).trim();
                            break;
                        }
                    }
                    writeToSet(dirtyName, tokens[1].toLowerCase(), "agi", writeSet);
                }
            }
        }

        // sci engine
        readFilePath = Paths.get(
                "/Users/vangos/Development/git/lazygalaxy-java/game/src/main/resources/scummvm/detection_tables.h/sci_detection_tables.h");
        scummLines = Files.readAllLines(readFilePath);
        for (int l = 0; l < scummLines.size(); l++) {
            String scummLine = scummLines.get(l).trim();
            if (StringUtils.startsWith(scummLine, "{")) {
                String[] tokens = StringUtils.split(scummLine, "{},");
                for (int i = 0; i < tokens.length; i++) {
                    tokens[i] = tokens[i].trim().replaceAll("\"", "");
                }
                if (tokens.length < 4) {
                    String dirtyName = null;
                    for (int j = l; ; j--) {
                        dirtyName = scummLines.get(j).trim();
                        if (StringUtils.isBlank(dirtyName)) {
                            dirtyName = scummLines.get(j + 1).trim();
                            break;
                        }
                    }
                    writeToSet(dirtyName, tokens[0].toLowerCase(), "sci", writeSet);
                }
            }
        }

        for (String line : writeSet) {
            Files.writeString(writeFilePath, line, StandardOpenOption.APPEND);
        }
    }

    private static void writeToSet(String dirtyName, String id, String engine, Set<String> writeSet) {
        if (StringUtils.startsWith(dirtyName, "//")) {
            String name = StringUtils.substring(dirtyName, 2).trim();
            if (StringUtils.contains(name, "-")) {
                name = StringUtils.substring(name, 0, name.indexOf("-")).trim();
            } else if (StringUtils.contains(name, "(")) {
                name = StringUtils.substring(name, 0, name.indexOf("(")).trim();
            }
            if (!StringUtils.containsAny(name.toLowerCase(), "reported by ", "demo ", "fan made")) {
                writeSet.add(id + SEPERATOR + engine + SEPERATOR + name + "\n");
            }
        }
    }
}
