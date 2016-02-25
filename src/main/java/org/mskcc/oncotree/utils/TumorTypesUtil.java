package org.mskcc.oncotree.utils;

import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Hongxin on 2/25/16.
 */
public class TumorTypesUtil {
    private static String TUMOR_TREE_FILE = "classpath:tumor_tree.txt";

    @Autowired
    private static ResourceLoader resourceLoader;

    public static Map<String, Object> getTumorTypes() {
        Map<String, Object> tumorTypes = new HashMap<>();
        TsvParserSettings settings = new TsvParserSettings();

        //the line separator sequence is defined here to ensure systems such as MacOS and Windows
        //are able to process this file correctly (MacOS uses '\r'; and Windows uses '\r\n').
        settings.getFormat().setLineSeparator("\n");

        // creates a TSV parser
        TsvParser parser = new TsvParser(settings);

        // parses all rows in one go.
        List<String[]> allRows = parser.parseAll(getReader(TUMOR_TREE_FILE));

        Map<String, Object> tumorType = new HashMap<>();
        tumorType.put("children", new HashMap<>());
        tumorType.put("code", "TISSUE");
        tumorType.put("name", "Tissue");

        //Iterate each row and assign tumor type to parent following the order of appearing
        for (String[] row : allRows.subList(1, allRows.size())) {
            tumorType.put("children", attachTumorType((Map<String, Object>) tumorType.get("children"), row, 0));
        }
        
        //Attach a root node in the JSON file
        tumorTypes.put("TISSUE", tumorType);
        return tumorTypes;
    }

    /**
     * Attach children to parent node.
     * @param tumorTypes
     * @param row One row of text file
     * @param index Current index of row. It will be increased everytime this function has been called.
     * @return parent node.
     */
    private static Map<String, Object> attachTumorType(Map<String, Object> tumorTypes, String[] row, int index) {
        if (index < 5 && row.length > index && row[index] != null && !row[index].isEmpty()) {
            Map<String, String> result = parseCodeName(row[index]);
            if (result.containsKey("code")) {
                Map<String, Object> tumorType = new HashMap<>();
                if (!tumorTypes.containsKey(result.get("code"))) {
                    tumorType.put("children", new HashMap<>());
                    tumorType.put("code", result.get("code"));
                    tumorType.put("name", result.get("name"));
                    tumorType.put("mainType", row.length > 5 ? row[5] : "");
                    tumorType.put("color", row.length > 6 ? row[6] : "");
                    tumorType.put("nci", row.length > 7 ? row[7] : "");
                    tumorType.put("umls", row.length > 8 ? row[8] : "");
                } else {
                    tumorType = (Map<String, Object>) tumorTypes.get(result.get("code"));
                }
                tumorType.put("children", attachTumorType((Map<String, Object>) tumorType.get("children"), row, ++index));
                tumorTypes.put(result.get("code"), tumorType);
            }
        }
        return tumorTypes;
    }

    /**
     * Parsing cell content into tumor type code and tumor type name.
     * @param content One cell of each row. 
     * @return The map of current tumor type. It includes 'code' and 'name'.
     */
    private static HashMap<String, String> parseCodeName(String content) {
        HashMap<String, String> result = new HashMap<>();

        Pattern pattern = Pattern.compile("([^\\(]+)\\(([^\\)]+)\\)");

        Matcher matcher = pattern.matcher(content);
        if (matcher.matches()) {
            result.put("name", matcher.group(1).trim());
            result.put("code", matcher.group(2).trim());
        }
        return result;
    }

    /**
     * Get tumor type text file input stream.
     * @param relativePath Tumor type text file path.
     * @return Input stream
     */
    private static InputStreamReader getReader(String relativePath) {
        try {
            return new InputStreamReader(resourceLoader.getResource(relativePath).getInputStream(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
