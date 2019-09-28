package servlet;

import org.apache.log4j.Logger;
import util.Constants;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Files;

@WebServlet("/file/*")
public class FileServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Logger log = Logger.getLogger(this.getClass().getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("file redirect");
        String filePathName = URLDecoder.decode(req.getPathInfo().substring(1), "UTF-8");
        int filePathNameIndex = filePathName.lastIndexOf("/");

        log.info("req.getPathInfo(): " + req.getPathInfo());
        log.info("filePathName: " + filePathName);

        String filePath = "";
        String fileName = "";

        if (filePathNameIndex == -1) {
            filePath = "";
            fileName = filePathName;
        } else {
            filePath = "/" + filePathName.substring(0, filePathNameIndex);
            fileName = filePathName.substring(filePathNameIndex + 1);
        }

        log.info("file path: " + filePath);
        log.info("file name: " + fileName);
        File file = new File(Constants.IMAGE_UPLOAD_URL + filePath, fileName);
        resp.setHeader("Content-Type", getServletContext().getMimeType(fileName));
        resp.setHeader("Content-Length", String.valueOf(file.length()));
        resp.setHeader("Content-Disposition", "inline; filename=\"" + file.getName() + "\"");

        Files.copy(file.toPath(), resp.getOutputStream());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("request success post");
        super.doPost(req, resp);
    }
}
