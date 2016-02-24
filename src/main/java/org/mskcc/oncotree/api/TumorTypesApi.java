package org.mskcc.oncotree.api;

import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import org.mskcc.oncotree.model.InlineResponse200;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping(value = "/tumorTypes", produces = {APPLICATION_JSON_VALUE})
@Api(value = "/tumorTypes", description = "the tumorTypes API")
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-02-24T21:00:49.581Z")
public class TumorTypesApi {


    @ApiOperation(value = "Tumor Types", notes = "Return all available tumor types.", response = InlineResponse200.class)
    @ApiResponses(value = {
        @io.swagger.annotations.ApiResponse(code = 200, message = "Nested tumor types object."),
        @io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error")})
    @RequestMapping(value = "",
        produces = {"application/json"},

        method = RequestMethod.GET)

    public ResponseEntity<Object> tumorTypesGet()
        throws Exception {

        if (TumorTypes == null) {
            TumorTypes = parseTumorTypes();
        }

        return new ResponseEntity<Object>(TumorTypes, HttpStatus.OK);
    }

    public Map<String, Object> TumorTypes = null;
    public String TUMOR_TREE_FILE = "classpath:tumor_tree.txt";

    @Autowired
    private ResourceLoader resourceLoader;

    private Map<String, Object> parseTumorTypes() throws Exception {
        Map<String, Object> tumorTypes = new HashMap<>();
        TsvParserSettings settings = new TsvParserSettings();
        //the file used in the example uses '\n' as the line separator sequence.
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

        for (String[] row : allRows.subList(1, allRows.size())) {
            tumorType.put("children", injectTumroType((Map<String, Object>) tumorType.get("children"), row, 0));
        }
        tumorTypes.put("TISSUE", tumorType);
        return tumorTypes;
    }

    private Map<String, Object> injectTumroType(Map<String, Object> tumorTypes, String[] row, int index) {
        if (index < 5 && row.length>index && row[index] != null && !row[index].isEmpty()) {
            Map<String, String> result = parseCodeName(row[index]);
            if (result.containsKey("code")) {
                Map<String, Object> tumorType = new HashMap<>();
                if (!tumorTypes.containsKey(result.get("code"))) {
                    tumorType.put("children", new HashMap<>());
                    tumorType.put("code", result.get("code"));
                    tumorType.put("name", result.get("name"));
                    tumorType.put("mainType", row.length>5?row[5]:"");
                    tumorType.put("color", row.length>6?row[6]:"");
                    tumorType.put("nci", row.length>7?row[7]:"");
                    tumorType.put("umls", row.length>8?row[8]:"");
                } else {
                    tumorType = (Map<String, Object>) tumorTypes.get(result.get("code"));
                }
                tumorType.put("children", injectTumroType((Map<String, Object>) tumorType.get("children"), row, ++index));
                tumorTypes.put(result.get("code"), tumorType);
            }
        }
        return tumorTypes;
    }

    private HashMap<String, String> parseCodeName(String content) {
        HashMap<String, String> result = new HashMap<>();

        Pattern pattern = Pattern.compile("([^\\(]+)\\(([^\\)]+)\\)");

        Matcher matcher = pattern.matcher(content);
        if (matcher.matches()) {
            result.put("name", matcher.group(1).trim());
            result.put("code", matcher.group(2).trim());
        }
        return result;
    }

    private InputStreamReader getReader(String relativePath) {
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
