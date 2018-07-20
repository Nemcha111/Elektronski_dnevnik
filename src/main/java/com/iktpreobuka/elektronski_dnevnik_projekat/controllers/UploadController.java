package com.iktpreobuka.elektronski_dnevnik_projekat.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/API/version1/")
public class UploadController {
	
	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	
//	@RequestMapping(value = "/download", method = RequestMethod.GET)
//	public class DownloadServlet extends HttpServlet {
//	    private final int ARBITARY_SIZE = 1048;
//	 
//	    @Override
//	    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
//	      throws ServletException, IOException {
//	     
//	        resp.setContentType("log/plain");
//	        resp.setHeader("Content-disposition", "attachment; filename=spring-boot-logging.log");
//	 
//	        try(InputStream in = req.getServletContext().getResourceAsStream("logs/spring-boot-logging.log");
//	          OutputStream out = resp.getOutputStream()) {
//	 
//	            byte[] buffer = new byte[ARBITARY_SIZE];
//	         
//	            int numBytesRead;
//	            while ((numBytesRead = in.read(buffer)) > 0) {
//	                out.write(buffer, 0, numBytesRead);
//	            }
//	        }
//	    }
	}
	
	
	
	
	/*@GetMapping(value = "/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public @ResponseBody byte[] getFile() throws IOException {

		InputStream in = getClass().getResourceAsStream("/logs/spring-boot-logging.log");
		return IOUtils.toByteArray(in);
	}
	*/
//}


/*
 * private final Logger logger = (Logger)
 * LoggerFactory.getLogger(this.getClass());
 * 
 * @Autowired private FileHandler fileHandler;
 * 
 * 
 * @RequestMapping(method = RequestMethod.GET) public String index() { return
 * "upload"; }
 * 
 * @RequestMapping(method = RequestMethod.GET, value = "/uploadStatus") public
 * String uploadStatus() { return "uploadStatus"; }
 * 
 * @RequestMapping(method = RequestMethod.POST, value = "/upload") public String
 * singleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes
 * redirectAttributes) { logger.debug("This is a debug message");
 * logger.info("this is a info message"); String result = null; try { result =
 * fileHandler.singleFileUpload(file, redirectAttributes); } catch (IOException
 * e) { e.printStackTrace(); } return result;
 * 
 * }
 */
// }

/*
 * @GetMapping(value = "/download") public ResponseEntity<Resource>
 * downloadUsersList() { try { File file = fileHandler.writeUsersListToFile();
 * 
 * InputStreamResource resource = new InputStreamResource(new
 * FileInputStream(file));
 * 
 * HttpHeaders responseHeaders = new HttpHeaders();
 * responseHeaders.add("content-disposition", "attachment; filename=" +
 * "userList.txt");
 * 
 * 
 * return ResponseEntity.ok() .headers(responseHeaders)
 * .contentLength(file.length())
 * .contentType(MediaType.parseMediaType("application/octet-stream"))
 * .body(resource); } catch (IOException e) { e.getStackTrace(); } return null;
 * }
 * 
 * }
 * 
 */

/*
 * import java.io.IOException;
 * 
 * import org.slf4j.Logger; import org.slf4j.LoggerFactory; import
 * org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.stereotype.Controller; import
 * org.springframework.web.bind.annotation.RequestMapping; import
 * org.springframework.web.bind.annotation.RequestMethod; import
 * org.springframework.web.bind.annotation.RequestParam; import
 * org.springframework.web.multipart.MultipartFile; import
 * org.springframework.web.servlet.mvc.support.RedirectAttributes;
 * 
 * import com.iktpreobuka.elektronski_dnevnik_projekat.services.FileHandler;
 * 
 * @Controller
 * 
 * @RequestMapping(path = "/") public class UploadController {
 * 
 * private final Logger logger =
 * (Logger)LoggerFactory.getLogger(this.getClass());
 * 
 * 
 * 
 * @Autowired private FileHandler fileHandler;
 * 
 * @RequestMapping(method = RequestMethod.GET) public String index() { return
 * "upload"; }
 * 
 * @RequestMapping(method = RequestMethod.GET, value = "/uploadStatus") public
 * String uploadStatus() { return "uploadStatus"; }
 * 
 * @RequestMapping(method = RequestMethod.POST, value = "/upload") public String
 * singleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes
 * redirectAttributes) { //("file") -- ovo je elian, samo dodeljuje ime
 * parametru. AKo se ne stavi zvao bi se kao naziv parametra
 * 
 * 
 * logger.debug("This is a debug message");
 * logger.info("This is an info message");
 * logger.warn("This is a warn message");
 * logger.error("This is an error message");
 * 
 * 
 * String result = null;
 * 
 * try { result = fileHandler.singleFileUpload(file, redirectAttributes);
 * 
 * } catch (IOException e) { e.printStackTrace(); }
 * 
 * return result;
 * 
 * } }
 * 
 */
