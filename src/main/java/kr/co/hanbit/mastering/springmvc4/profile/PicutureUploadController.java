package kr.co.hanbit.mastering.springmvc4.profile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.hanbit.mastering.springmvc4.config.PicturesUploadProperties;

@Controller
public class PicutureUploadController {
    private final Resource pictureDir;
    private final Resource anonymousPicture;

    @Autowired
    public PicutureUploadController(PicturesUploadProperties picturesUploadProperties) {
        this.pictureDir = picturesUploadProperties.getUploadPath();
        this.anonymousPicture = picturesUploadProperties.getAnonymousPicture();
    }

    @ModelAttribute("picturePath")
    public Resource picturePath() {
        return anonymousPicture;
    }

    @RequestMapping("upload")
    public String uploadPage() {
        return "profile/upload-page";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String onUpload(MultipartFile file, RedirectAttributes redirectAttrs, Model model) throws IOException {

        if (file.isEmpty() || !isImage(file)) {
            redirectAttrs.addFlashAttribute("error", "Incorrect file.Please upload a picture.");
            return "redirect:/upload";
        }

        Resource picturePath = copyFileToPictures(file);
        model.addAttribute("picturePath", picturePath);

        return "profile/upload-page";
    }

    @RequestMapping(value = "/uploaded-picture")
    public void getUploadedPicture(HttpServletResponse response, @ModelAttribute("picturePath") Path picturePath)
            throws IOException {
        response.setHeader("Content-Type", URLConnection.guessContentTypeFromName(picturePath.toString()));
        Files.copy(picturePath, response.getOutputStream());
    }

    private Resource copyFileToPictures(MultipartFile file) throws IOException {
        String fileExtension = getFileExtension(file.getOriginalFilename());
        File tempFile = File.createTempFile("pic", fileExtension, pictureDir.getFile());

        try (InputStream in = file.getInputStream(); OutputStream out = new FileOutputStream(tempFile)) {
            IOUtils.copy(in, out);
        }

        return new FileSystemResource(tempFile);
    }

    private boolean isImage(MultipartFile file) {
        return file.getContentType().startsWith("image");
    }

    private static String getFileExtension(String name) {
        return name.substring(name.lastIndexOf("."));
    }
}
