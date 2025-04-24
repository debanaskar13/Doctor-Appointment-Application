package site.debashisnaskar.rxflow.utils;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class CloudinaryUtil {

    private static final Cloudinary cloudinary;
    private static final Dotenv dotenv = Dotenv.load();

    static {
        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", dotenv.get("CLOUDINARY_CLOUD_NAME"),
                "api_key",dotenv.get("CLOUDINARY_API_KEY"),
                "api_secret",dotenv.get("CLOUDINARY_SECRET_KEY")
        ));
    }

    public static Cloudinary getInstance() {
        return cloudinary;
    }

    public static String uploadToCloudinary(Part part) throws IOException {
        File file = Utils.convertPartToFile(part);
        Map uploadResult = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());

        String imageUrl = uploadResult.get("secure_url").toString();
        file.delete();

        return imageUrl;
    }

    public static boolean deleteFromCloudinary(String imageId) {
        try {

            Map map = cloudinary.uploader().destroy(imageId, ObjectUtils.emptyMap());
            System.out.println(map);
            return true;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
