package org.mskcc.oncotree.api;

import org.mskcc.oncotree.model.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.http.MediaType.*;

@Controller
@RequestMapping(value = "/tumorType", produces = {APPLICATION_JSON_VALUE})
@Api(value = "/tumorType", description = "the tumorType API")
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-02-24T21:00:49.581Z")
public class TumorTypeApi {
  

  @ApiOperation(value = "Tumor Types", notes = "...", response = TumorType.class, responseContainer = "List")
  @ApiResponses(value = {
    @io.swagger.annotations.ApiResponse(code = 200, message = "An array of tumor types"),
    @io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error") })
  @RequestMapping(value = "", 
    produces = { "application/json" }, 
    
    method = RequestMethod.GET)
  public ResponseEntity<List<TumorType>> tumorTypeGet(@ApiParam(value = "Query type. It could be 'code', 'name', 'mainType', 'nci', 'umls' or 'color'", required = true) @RequestParam(value = "queryType", required = true) String queryType


,
    @ApiParam(value = "The query content", required = true) @RequestParam(value = "query", required = true) String query


)
      throws NotFoundException {
      // do some magic!
      return new ResponseEntity<List<TumorType>>(HttpStatus.OK);
  }

  

  @ApiOperation(value = "Tumor Types", notes = "...", response = TumorType.class, responseContainer = "List")
  @ApiResponses(value = {
    @io.swagger.annotations.ApiResponse(code = 200, message = "An array of tumor types"),
    @io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error") })
  @RequestMapping(value = "", 
    produces = { "application/json" }, 
    consumes = { "application/json" },
    method = RequestMethod.POST)
  public ResponseEntity<List<TumorType>> tumorTypePost(

@ApiParam(value = "" ,required=true ) @RequestBody TumorTypeQueries query
)
      throws NotFoundException {
      // do some magic!
      return new ResponseEntity<List<TumorType>>(HttpStatus.OK);
  }

  
}
