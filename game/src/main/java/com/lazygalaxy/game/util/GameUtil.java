package com.lazygalaxy.game.util;

import com.lazygalaxy.engine.helper.MongoHelper;
import com.lazygalaxy.engine.util.GeneralUtil;
import com.lazygalaxy.game.Constant.Genre;
import com.lazygalaxy.game.Constant.SubGenre;
import com.lazygalaxy.game.domain.Game;
import com.lazygalaxy.game.domain.GameInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.conversions.Bson;

import java.util.List;

public class GameUtil {
    private static final Logger LOGGER = LogManager.getLogger(GameUtil.class);

    public static void pretifyName(GameInfo info) {
        if (!StringUtils.isBlank(info.originalName)) {
            String processName = info.originalName;
            info.version = null;
            info.names = null;

            int index = Integer.MAX_VALUE;
            if (StringUtils.contains(processName, ")")) {
                index = Math.min(index, processName.indexOf("("));
            }

            if (StringUtils.contains(processName, "]")) {
                index = Math.min(index, processName.indexOf("["));
            }

            if (index != Integer.MAX_VALUE) {
                info.version = StringUtils.substring(processName, index, processName.length());
                processName = StringUtils.substring(processName, 0, index);
            }

            for (String name : StringUtils.splitByWholeSeparator(processName, " / ")) {
                name = pretify(name);
                if (StringUtils.contains(name, ": ")) {
                    String uniqueNameBefore = StringUtils.substring(name, 0, name.lastIndexOf(": "));
                    String uniqueNameAfter = uniqueNameBefore.replaceAll(": ", " ");
                    if (StringUtils.startsWith(uniqueNameAfter.toLowerCase(), "the ")) {
                        uniqueNameAfter = StringUtils.right(uniqueNameAfter, uniqueNameAfter.length() - 4) + ", The";
                    }
                    name = StringUtils.replace(name, uniqueNameBefore, uniqueNameAfter);
                    info.names = SetUtil.addValueToArrayList(info.names, name);
                    info.names = SetUtil.addValueToArrayList(info.names, uniqueNameAfter);
                    if (StringUtils.endsWith(uniqueNameAfter.toLowerCase(), ", the")) {
                        String smallerName = StringUtils.left(uniqueNameAfter, uniqueNameAfter.length() - 5);
                        info.names = SetUtil.addValueToArrayList(info.names,
                                smallerName + StringUtils.substring(name, name.lastIndexOf(": "), name.length()));
                        info.names = SetUtil.addValueToArrayList(info.names, smallerName);
                    }
                } else {
                    if (StringUtils.startsWith(name.toLowerCase(), "the ")) {
                        name = StringUtils.right(name, name.length() - 4) + ", The";
                    }
                    info.names = SetUtil.addValueToArrayList(info.names, name);
                    if (StringUtils.endsWith(name.toLowerCase(), ", the")) {
                        info.names = SetUtil.addValueToArrayList(info.names, StringUtils.left(name, name.length() - 5));
                    }
                }
            }

            if (info.version != null) {
                info.version = info.version.replaceAll("\\)", " ");
                info.version = info.version.replaceAll("\\(", " ");
                info.version = info.version.replaceAll("\\]", " ");
                info.version = info.version.replaceAll("\\[", " ");

                while (StringUtils.contains(info.version, "  ")) {
                    info.version = info.version.replaceAll("  ", " ");
                }

                info.version = info.version.trim();
            }

        } else {
            info.originalName = null;
            info.names = null;
            info.version = null;
        }
    }

    private static String pretify(String str) {
        if (!StringUtils.isBlank(str)) {
            // str = str.replaceAll("\n", " ");
            // str = str.replaceAll("\r", " ");
            // str = str.replaceAll("\\.", ". ");
            str = str.replaceAll("&amp;", "&");
            str = str.replaceAll(" and ", " & ");
            str = str.replaceAll(" And ", " & ");
            str = str.replaceAll("\\)", " ");
            str = str.replaceAll("\\(", " ");
            str = str.replaceAll("\\]", " ");
            str = str.replaceAll("\\[", " ");

            str = str.replaceAll(" - ", " : ");
            str = str.replaceAll("- ", ": ");
            str = str.replaceAll(" -", " :");

            while (StringUtils.contains(str, " :")) {
                str = str.replaceAll(" :", ":");
            }
            str = str.replaceAll(":", ": ");
            while (StringUtils.contains(str, "  ")) {
                str = str.replaceAll("  ", " ");
            }
            str = str.replaceAll("':", ":");

            // numbers
            str = str.replaceAll(" I:", " 1:");
            str = str.replaceAll(" II:", " 2:");
            str = str.replaceAll(" III:", " 3:");
            str = str.replaceAll(" IV:", " 4:");
            str = str.replaceAll(" V:", " 5:");
            str = str.replaceAll(" VI:", " 6:");
            str = str.replaceAll(" VII:", " 7:");
            str = str.replaceAll(" VIII:", " 8:");
            str = str.replaceAll(" IX:", " 9:");

            str = str.trim();
            str = str.replaceAll(" I$", " 1");
            str = str.replaceAll(" II$", " 2");
            str = str.replaceAll(" III$", " 3");
            str = str.replaceAll(" IV$", " 4");
            str = str.replaceAll(" V$", " 5");
            str = str.replaceAll(" VI$", " 6");
            str = str.replaceAll(" VII$", " 7");
            str = str.replaceAll(" VIII$", " 8");
            str = str.replaceAll(" IX$", " 9");

            str = str.trim();
            return str;
        }
        return null;
    }

    public static List<Game> getGames(boolean printNoGameFound, boolean printMultipleGamesFound, String identifier,
                                      Bson sort, Bson... find) throws Exception {
        List<Game> games = MongoHelper.getHelper(Game.class).getDocumentsByFilters(sort, find);

        if (games.size() == 0) {
            if (printNoGameFound) {
                LOGGER.warn("game not found: " + identifier);
            }
            return null;
        } else if (games.size() > 1) {
            String gameIds = "";
            for (Game game : games) {
                gameIds += " " + game.id;
            }

            if (printMultipleGamesFound) {
                LOGGER.warn("multiple games found: " + identifier + " with roms: " + gameIds);
            }
        }

        return games;
    }

    public static String normalizeManufacturer(String manufacturer, boolean returnOther) {
        if (StringUtils.contains(GeneralUtil.alphanumerify(manufacturer), "alphadenshi")) {
            return "Alpha Denshi";
        }

        if (StringUtils.contains(GeneralUtil.alphanumerify(manufacturer), "atari")) {
            return "Atari";
        }

        if (StringUtils.contains(GeneralUtil.alphanumerify(manufacturer), "atlus")) {
            return "Atlus";
        }

        if (StringUtils.contains(GeneralUtil.alphanumerify(manufacturer), "banpresto")) {
            return "Banpresto";
        }

        if (StringUtils.contains(GeneralUtil.alphanumerify(manufacturer), "capcom")) {
            return "Capcom";
        }

        if (StringUtils.contains(GeneralUtil.alphanumerify(manufacturer), "cave")) {
            return "Cave";
        }

        if (StringUtils.contains(GeneralUtil.alphanumerify(manufacturer), "dataeast")) {
            return "Data East";
        }

        if (StringUtils.contains(GeneralUtil.alphanumerify(manufacturer), "eighting") ||
                StringUtils.contains(GeneralUtil.alphanumerify(manufacturer), "raizing")) {
            return "Eighting";
        }

        if (StringUtils.contains(GeneralUtil.alphanumerify(manufacturer), "electronicarts")) {
            return "Electronic Arts";
        }

        if (StringUtils.contains(GeneralUtil.alphanumerify(manufacturer), "gaelco")) {
            return "Gaelco";
        }

        if (StringUtils.contains(GeneralUtil.alphanumerify(manufacturer), "gottlieb")) {
            return "Gottlieb";
        }

        if (StringUtils.contains(GeneralUtil.alphanumerify(manufacturer), "igs")) {
            return "IGS";
        }

        if (StringUtils.contains(GeneralUtil.alphanumerify(manufacturer), "incredibletechnologies")) {
            return "Incredible Technologies";
        }

        if (StringUtils.contains(GeneralUtil.alphanumerify(manufacturer), "irem")) {
            return "Irem";
        }

        if (StringUtils.contains(GeneralUtil.alphanumerify(manufacturer), "jaleco")) {
            return "Jaleco";
        }

        if (StringUtils.contains(GeneralUtil.alphanumerify(manufacturer), "kaneko")) {
            return "Kaneko";
        }

        if (StringUtils.contains(GeneralUtil.alphanumerify(manufacturer), "konami")) {
            return "Konami";
        }

        if (StringUtils.contains(GeneralUtil.alphanumerify(manufacturer), "midway")) {
            return "Midway";
        }

        if (StringUtils.contains(GeneralUtil.alphanumerify(manufacturer), "mitchell")) {
            return "Mitchell";
        }

        if (StringUtils.contains(GeneralUtil.alphanumerify(manufacturer), "namco")) {
            return "Namco";
        }

        if (StringUtils.contains(GeneralUtil.alphanumerify(manufacturer), "nazca")) {
            return "Nazca";
        }

        if (StringUtils.contains(GeneralUtil.alphanumerify(manufacturer), "nichibutsu")) {
            return "Nichibutsu";
        }

        if (StringUtils.contains(GeneralUtil.alphanumerify(manufacturer), "nintendo")) {
            return "Nintendo";
        }

        if (StringUtils.contains(GeneralUtil.alphanumerify(manufacturer), "nmk")) {
            return "NMK";
        }

        if (StringUtils.contains(GeneralUtil.alphanumerify(manufacturer), "orientalsoft")) {
            return "Oriental Soft";
        }

        if (StringUtils.contains(GeneralUtil.alphanumerify(manufacturer), "psikyo")) {
            return "Psikyo";
        }

        if (StringUtils.contains(GeneralUtil.alphanumerify(manufacturer), "romstar")) {
            return "Romstar";
        }

        if (StringUtils.contains(GeneralUtil.alphanumerify(manufacturer), "sega")) {
            return "SEGA";
        }

        if (StringUtils.contains(GeneralUtil.alphanumerify(manufacturer), "snk") || StringUtils.contains(GeneralUtil.alphanumerify(manufacturer), "playmore")) {
            return "SNK";
        }

        if (StringUtils.contains(GeneralUtil.alphanumerify(manufacturer), "sammy")) {
            return "Sammy";
        }

        if (StringUtils.contains(GeneralUtil.alphanumerify(manufacturer), "sternelectronics")) {
            return "Stern Electronics";
        }

        if (StringUtils.contains(GeneralUtil.alphanumerify(manufacturer), "seibukaihatsu")) {
            return "Seibu Kaihatsu";
        }

        if (StringUtils.contains(GeneralUtil.alphanumerify(manufacturer), "tadcorporation")) {
            return "TAD Corporation";
        }

        if (StringUtils.contains(GeneralUtil.alphanumerify(manufacturer), "taito")) {
            return "Taito";
        }

        if (StringUtils.contains(GeneralUtil.alphanumerify(manufacturer), "technosjapan")) {
            return "Technos Japan";
        }

        if (StringUtils.contains(GeneralUtil.alphanumerify(manufacturer), "tehkan")) {
            return "Tehkan";
        }

        if (StringUtils.contains(GeneralUtil.alphanumerify(manufacturer), "tecmo")) {
            return "Tecmo";
        }

        if (StringUtils.contains(GeneralUtil.alphanumerify(manufacturer), "toaplan")) {
            return "Toaplan";
        }

        if (StringUtils.contains(GeneralUtil.alphanumerify(manufacturer), "universal")) {
            return "Universal";
        }

        if (StringUtils.contains(GeneralUtil.alphanumerify(manufacturer), "upl")) {
            return "UPL";
        }

        if (StringUtils.contains(GeneralUtil.alphanumerify(manufacturer), "videosystem") ||
                StringUtils.contains(GeneralUtil.alphanumerify(manufacturer), "vsystem")) {
            return "Video System";
        }

        if (StringUtils.contains(GeneralUtil.alphanumerify(manufacturer), "virgin")) {
            return "Virgin";
        }

        if (StringUtils.contains(GeneralUtil.alphanumerify(manufacturer), "visco")) {
            return "Visco";
        }

        if (StringUtils.contains(GeneralUtil.alphanumerify(manufacturer), "williams")) {
            return "Williams";
        }

        if (returnOther) {
            return "Other";
        }

        if (StringUtils.endsWith(GeneralUtil.alphanumerify(manufacturer, ".", ""), "inc.")) {
            return clean(StringUtils.replaceIgnoreCase(manufacturer, "inc.", "").trim());
        }

        if (StringUtils.endsWith(GeneralUtil.alphanumerify(manufacturer, ".", ""), "ltd.")) {
            return clean(StringUtils.replaceIgnoreCase(manufacturer, "ltd.", "").trim());
        }

        if (StringUtils.endsWith(GeneralUtil.alphanumerify(manufacturer, ".", ""), "co.")) {
            return clean(StringUtils.replaceIgnoreCase(manufacturer, "co.", "").trim());
        }

        if (StringUtils.endsWith(GeneralUtil.alphanumerify(manufacturer), "license")) {
            return normalizeManufacturer(clean(StringUtils.replaceIgnoreCase(manufacturer, "license", "").trim()), false);
        }

        if (StringUtils.equals(GeneralUtil.alphanumerify(manufacturer), "bootleg") || StringUtils.equals(GeneralUtil.alphanumerify(manufacturer), "hack")) {
            return null;
        }

        return manufacturer;
    }

    private static String clean(String value) {
        if (StringUtils.endsWith(value, ",")) {
            return StringUtils.substring(value, 0, value.length() - 1);
        }
        return value;
    }

    public static Pair<String, String> normalizeGenres(String genre, String subGenre, String name, boolean returnNull) {
        String simplifyGenre = GeneralUtil.alphanumerify(genre);
        String simplifySubGenre = GeneralUtil.alphanumerify(subGenre);
        String simplifyName = GeneralUtil.alphanumerify(name);

        if (StringUtils.contains(simplifyGenre, "beatemup")) {
            return Pair.of(Genre.BEATEMUP, subGenre);
        }

        if (StringUtils.contains(simplifyGenre, "shootemup")) {
            return Pair.of(Genre.SHOOTEMUP, subGenre);
        }

        if (StringUtils.contains(simplifyGenre, "army")) {
            if (StringUtils.containsAny(simplifySubGenre, "airforce", "helicopter")) {
                return Pair.of(Genre.SHOOTEMUP, SubGenre.AIRCRAFT);
            } else if (StringUtils.contains(simplifySubGenre, "tank")) {
                return Pair.of(Genre.SHOOTEMUP, SubGenre.TANK);
            } else if (StringUtils.contains(simplifySubGenre, "fighter")) {
                return Pair.of(Genre.RUNNGUN, SubGenre.ARMY);
            }
        }

        if (StringUtils.containsAny(simplifyGenre, "fight", "fighting")) {
            if (StringUtils.contains(simplifyName, "kingoffighter")) {
                return Pair.of(Genre.FIGHTER, SubGenre.KINGOFFIGHTERS);
            } else if (StringUtils.contains(simplifyName, "streetfighter")) {
                return Pair.of(Genre.FIGHTER, SubGenre.STREETFIGHTER);
            } else if (StringUtils.containsAny(simplifySubGenre, "streetfighter", "kingoffighter")) {
                return Pair.of(Genre.FIGHTER, SubGenre.OTHER);
            }

            if (StringUtils.containsAny(simplifySubGenre, "asianvs", "asian3d")) {
                return Pair.of(Genre.FIGHTER, SubGenre.ASIAN);
            }

            if (StringUtils.containsAny(simplifySubGenre, "hero", "warriors", "asian")) {
                return Pair.of(Genre.BEATEMUP, cleanSubGenre(subGenre));
            }
        }

        if (StringUtils.contains(simplifyGenre, "maze")) {
            return Pair.of(Genre.MAZE, cleanSubGenre(subGenre));
        }

        if (StringUtils.contains(simplifyGenre, "platform")) {
            return Pair.of(Genre.PLATFORM, cleanSubGenre(subGenre));
        }

        if (StringUtils.contains(simplifyGenre, "puzzle")) {
            if (StringUtils.contains(simplifyName, "tetris") || StringUtils.contains(simplifySubGenre, "tetris")) {
                return Pair.of(Genre.PUZZLE, SubGenre.TETRIS);
            }
            return Pair.of(Genre.PUZZLE, cleanSubGenre(subGenre));
        }

        if (StringUtils.containsAny(simplifyGenre, "race", "racing", "drive", "driving")) {
            if (StringUtils.containsAny(simplifySubGenre, "car", "f1", "2d", "3d", "drive", "driving", "demolition")) {
                return Pair.of(Genre.RACING, SubGenre.CAR);
            }
            return Pair.of(Genre.RACING, cleanSubGenre(subGenre));
        }

        if (StringUtils.contains(simplifyGenre, "space")) {
            if (StringUtils.containsAny(simplifySubGenre, "force", "defender", "rtype", "basedefense", "aliens")) {
                return Pair.of(Genre.SHOOTEMUP, SubGenre.SPACECRAFT);
            }
            if (StringUtils.contains(simplifySubGenre, "robot")) {
                return Pair.of(Genre.RUNNGUN, SubGenre.ROBOT);
            }
        }

        if (StringUtils.contains(simplifyGenre, "sport")) {
            if (StringUtils.equals(simplifySubGenre, "1")) {
                return Pair.of(Genre.SPORTS, SubGenre.TRACKANDFIELD);
            } else if (StringUtils.equals(simplifySubGenre, "2")) {
                return Pair.of(Genre.SPORTS, SubGenre.OTHER);
            }
            return Pair.of(Genre.SPORTS, cleanSubGenre(subGenre));
        }

        if (returnNull) {
            return Pair.of(null, null);
        }
        return Pair.of(genre, subGenre);
    }

    private static String cleanSubGenre(String subGenre) {
        subGenre = subGenre.replaceAll(" 1$", "");
        subGenre = subGenre.replaceAll(" 2$", "");
        subGenre = subGenre.replaceAll(" 3$", "");
        subGenre = subGenre.replaceAll(" 4$", "");
        subGenre = subGenre.replaceAll(" 5$", "");
        subGenre = subGenre.replaceAll(" 6$", "");
        subGenre = subGenre.replaceAll(" 7$", "");
        subGenre = subGenre.replaceAll(" 8$", "");
        subGenre = subGenre.replaceAll(" 9$", "");

        return subGenre;
    }
}
