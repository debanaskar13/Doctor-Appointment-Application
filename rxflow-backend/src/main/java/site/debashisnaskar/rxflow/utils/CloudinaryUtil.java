package site.debashisnaskar.rxflow.utils;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import io.github.cdimascio.dotenv.Dotenv;

public class CloudinaryUtil {

    private static final Cloudinary cloudinary;
    private static final Dotenv dotenv = Dotenv.load();

    static {
        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "could_name", dotenv.get("CLOUDINARY_CLOUD_NAME"),
                "api_key",dotenv.get("CLOUDINARY_API_KEY"),
                "api_secret",dotenv.get("CLOUDINARY_SECRET_KEY")
        ));
    }

    public static Cloudinary getInstance() {
        return cloudinary;
    }

}
