package filemanager;
 
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
 
 
 
@Controller
public class FileController {
    
   @Autowired
   private fileObjectRepository filerepo;
    
 
 
 @GetMapping("/files")
    public String list(Model model) {
        model.addAttribute("files", this.filerepo.findAll(Sort.by(Sort.Direction.ASC, "name")));
        return "files";
    }
    
 
@PostMapping("/files")
public String save(@RequestParam("file") MultipartFile file) throws IOException {
    FileObject fo = new FileObject();
 
    fo.setName(file.getOriginalFilename());
    fo.setMediaType(file.getContentType());
    fo.setSize(file.getSize());
    fo.setContent(file.getBytes());
    if(fo.getSize()!=0)
    filerepo.save(fo);
 
    return "redirect:/files";
}
 
@GetMapping("/files/{id}")
public ResponseEntity<byte[]> viewFile(@PathVariable Long id) {
    FileObject fo = filerepo.getOne(id);
 
    final HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.parseMediaType(fo.getMediaType()));
    headers.setContentLength(fo.getSize());
    headers.add("Content-Disposition", "attachment; filename=" + fo.getName());
 
    return new ResponseEntity<>(fo.getContent(), headers, HttpStatus.CREATED);
}
    
    
}