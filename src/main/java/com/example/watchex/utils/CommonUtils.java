package com.example.watchex.utils;

import com.example.watchex.dto.SearchDto;
import com.example.watchex.entity.Admin;
import com.example.watchex.entity.User;
import com.example.watchex.service.CategoryService;
import org.joda.time.DateTime;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.NumberUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.*;
import java.util.regex.Pattern;

public class CommonUtils {
    private static final CategoryService categoryService = new CategoryService();
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");
    private static final Pattern IMAGE = Pattern.compile("[^\\w-.]");

    public static String toSlug(String name) {
        String[] a = {"à", "á", "ạ", "ả", "ã", "â", "ầ", "ấ", "ậ", "ẩ", "ẫ", "ă", "ắ", "ằ", "ắ", "ặ", "ẳ", "ẵ", "a"};
        String[] d = {"đ", "d"};
        String[] e = {"è", "é", "ẹ", "ẻ", "ẽ", "ê", "ề", "ế", "ệ", "ể", "ễ", "e"};
        String[] i = {"ì", "í", "ị", "ỉ", "ĩ", "i"};
        String[] y = {"ỳ", "ý", "ỵ", "ỷ", "ỹ", "y"};
        String[] o = {"ò", "ó", "ọ", "ỏ", "õ", "ô", "ồ", "ố", "ộ", "ổ", "ỗ", "ơ", "ờ", "ớ", "ợ", "ở", "ỡ", "o"};
        String[] u = {"ù", "ú", "ụ", "ủ", "ũ", "ừ", "ứ", "ự", "ử", "ữ", "u", "ư"};
        name = name.replace(" ", "-");
        name = name.replace("#", "sharp");
        name = name.replace("$", "dola");
        String[] specialchars = {")", "(", "*", "[", "]", "}", "{", ">", "<", "=", ":", ",", "'", "\"", "/", "\\", "&",
                "?", ";", ".", "@", "^"};
        name = name.toLowerCase();
        for (int k = 0; k < specialchars.length; k++)
            name = name.replace(specialchars[k], "");
        for (int k = 0; k < a.length; k++)
            name = name.replace(a[k], "a");
        for (int k = 0; k < d.length; k++)
            name = name.replace(d[k], "d");
        for (int k = 0; k < e.length; k++)
            name = name.replace(e[k], "e");
        for (int k = 0; k < i.length; k++)
            name = name.replace(i[k], "i");
        for (int k = 0; k < y.length; k++)
            name = name.replace(y[k], "y");
        for (int k = 0; k < o.length; k++)
            name = name.replace(o[k], "o");
        for (int k = 0; k < u.length; k++)
            name = name.replace(u[k], "u");
        return name;
    }

    public static String toImageUrl(String input) {
        String nowhitespace = WHITESPACE.matcher(input).replaceAll("-");
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
        String slug = IMAGE.matcher(normalized).replaceAll("");
        return slug.toLowerCase(Locale.ENGLISH);
    }

    private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

    public static Pageable getPageRequest(SearchDto searchDto) {
        List<Sort.Order> orders = new ArrayList<>();
        if (searchDto == null) {
            searchDto = new SearchDto();
        }
        if (null == searchDto.getPageIndex() || searchDto.getPageIndex() < 1)
            searchDto.setPageIndex(1);
        if (null == searchDto.getPageSize() || searchDto.getPageSize() < 1)
            searchDto.setPageSize(10);
        if (null != searchDto.getAsc() && !searchDto.getAsc().isEmpty()) {
            orders.add(new Sort.Order(Sort.Direction.ASC, searchDto.getAsc()));
        } else if (!Objects.equals(searchDto.getDesc(), "")) {
            orders.add(new Sort.Order(Sort.Direction.DESC, searchDto.getDesc()));
        }
        return PageRequest.of(searchDto.getPageIndex() - 1, searchDto.getPageSize(), Sort.by(orders));
    }

    public static boolean isNull(Object obj) {
        return obj == null;
    }

    @Bean
    public static String encode(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }

    public static boolean isNotNull(Object obj) {
        return obj != null;
    }

    public static boolean isEmptsy(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isEmpty(Object[] array) {
        if (array == null) {
            return true;
        } else {
            return array.length <= 0;
        }
    }

    public static boolean isEmpty(String s) {
        return s == null || s.isEmpty();
    }

    public static boolean isEmpty(String s, boolean ignoreSpaces) {
        boolean var10000;
        if (s != null) {
            label29:
            {
                if (ignoreSpaces) {
                    if (s.trim().isEmpty()) {
                        break label29;
                    }
                } else if (s.isEmpty()) {
                    break label29;
                }

                var10000 = false;
                return var10000;
            }
        }

        var10000 = true;
        return var10000;
    }

    public static boolean isInLength(String s, int minLength, int maxLength) {
        if (minLength < 0) {
            minLength = 0;
        }

        if (maxLength < minLength) {
            maxLength = 2147483647;
        }

        if (isNull(s)) {
            return false;
        } else {
            int length = s.length();
            return length >= minLength && length <= maxLength;
        }
    }

    public static boolean isPositive(Long val, boolean greaterThanZero) {
        boolean var10000;
        label25:
        {
            if (val != null) {
                if (greaterThanZero) {
                    if (val > 0L) {
                        break label25;
                    }
                } else if (val >= 0L) {
                    break label25;
                }
            }

            var10000 = false;
            return var10000;
        }

        var10000 = true;
        return var10000;
    }

    public static boolean isPositive(Integer val, boolean greaterThanZero) {
        boolean var10000;
        label25:
        {
            if (val != null) {
                if (greaterThanZero) {
                    if (val > 0) {
                        break label25;
                    }
                } else if (val >= 0) {
                    break label25;
                }
            }

            var10000 = false;
            return var10000;
        }

        var10000 = true;
        return var10000;
    }

    public static boolean isPositive(Float val, boolean greaterThanZero) {
        boolean var10000;
        label25:
        {
            if (val != null) {
                if (greaterThanZero) {
                    if (val > 0.0F) {
                        break label25;
                    }
                } else if (val >= 0.0F) {
                    break label25;
                }
            }

            var10000 = false;
            return var10000;
        }

        var10000 = true;
        return var10000;
    }

    public static boolean isPositive(Double val, boolean greaterThanZero) {
        boolean var10000;
        label25:
        {
            if (val != null) {
                if (greaterThanZero) {
                    if (val > 0.0D) {
                        break label25;
                    }
                } else if (val >= 0.0D) {
                    break label25;
                }
            }

            var10000 = false;
            return var10000;
        }

        var10000 = true;
        return var10000;
    }

    public static boolean isProgress(Integer val) {
        return val != null && val >= 0 && val <= 100;
    }

    public static int parseInt(String val, final int defaultValue) {
        if (isEmpty(val)) {
            return defaultValue;
        } else {
            int ret = (Integer) NumberUtils.parseNumber(val, Integer.class);
            return ret;
        }
    }

    public static long parseLong(String val, final long defaultValue) {
        if (isEmpty(val)) {
            return defaultValue;
        } else {
            long ret = (Long) NumberUtils.parseNumber(val, Long.class);
            return ret;
        }
    }

    public static float parseFloat(String val, final float defaultValue) {
        if (isEmpty(val)) {
            return defaultValue;
        } else {
            float ret = (Float) NumberUtils.parseNumber(val, Float.class);
            return ret;
        }
    }

    public static double parseDouble(String val, final double defaultValue) {
        if (isEmpty(val)) {
            return defaultValue;
        } else {
            double ret = (Double) NumberUtils.parseNumber(val, Double.class);
            return ret;
        }
    }

    public static Long[] parseLong(String input, String delimiter, boolean dedup) {
        if (!isEmpty(input) && !isEmpty(delimiter)) {
            String[] splits = input.split(delimiter);
            Set<Long> vals = new HashSet();
            String[] var5 = splits;
            int var6 = splits.length;

            for (int var7 = 0; var7 < var6; ++var7) {
                String s = var5[var7];
                Long val = (Long) NumberUtils.parseNumber(s, Long.class);
                if (val > 0L) {
                    if (dedup && !vals.contains(val)) {
                        vals.add(val);
                    } else {
                        vals.add(val);
                    }
                }
            }

            return (Long[]) vals.toArray(new Long[0]);
        } else {
            return new Long[0];
        }
    }

    public static Long replaceNull(Long input, long defaultValue) {
        return isNull(input) ? defaultValue : input;
    }

    public static Double replaceNull(Double input, double defaultValue) {
        return isNull(input) ? defaultValue : input;
    }

    public static Integer replaceNull(Integer input, int defaultValue) {
        return isNull(input) ? defaultValue : input;
    }

    public static Float replaceNull(Float input, float defaultValue) {
        return isNull(input) ? defaultValue : input;
    }

    public static String replaceNull(String input, String defaultValue) {
        return isNull(input) ? defaultValue : input;
    }

    public static boolean valueEqual(Integer input1, Integer input2) {
        if (input1 != null && input2 != null) {
            return input1 == input2;
        } else {
            return false;
        }
    }

    public static boolean valueEqual(Long input1, Long input2) {
        if (input1 != null && input2 != null) {
            return input1 == input2;
        } else {
            return false;
        }
    }

    public static boolean valueEqual(DateTime input1, DateTime input2) {
        if (input1 != null && input2 != null) {
            return input1.getMillis() == input2.getMillis();
        } else {
            return false;
        }
    }

    public static String getDate(Date date, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }

    public static DateTime getDateWithoutTimezone(DateTime input) {
        if (isNull(input)) {
            return null;
        } else {
            Date date = input.toDate();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int offset = calendar.getTimeZone().getOffset(calendar.getTimeInMillis());
            calendar.add(14, offset);
            calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
            return new DateTime(calendar);
        }
    }

    public static String formatDateTime(DateTime input) {
        return sdf.format(input.toDate());
    }

    public static String byteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < (long) unit) {
            return bytes + " B";
        } else {
            int exp = (int) (Math.log((double) bytes) / Math.log((double) unit));
            String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
            return String.format("%.1f %sB", (double) bytes / Math.pow((double) unit, (double) exp), pre);
        }
    }

    public static String withSuffix(long count) {
        if (count < 1000L) {
            return "" + count;
        } else {
            int exp = (int) (Math.log((double) count) / Math.log(1000.0D));
            return String.format("%.1f %c", (double) count / Math.pow(1000.0D, (double) exp), "kMGTPE".charAt(exp - 1));
        }
    }

    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return CommonUtils.isNotNull(authentication) && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken);
    }

    public static User getCurrentUser() {
        if (!isAuthenticated()) {
            return null;
        } else {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            return (User) authentication.getPrincipal();
        }
    }

    public static Admin getCurrentAdmin() {
        if (!isAuthenticated()) {
            return null;
        } else {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            return (Admin) authentication.getPrincipal();
        }
    }

    public static String randomString(Integer targetStringLength) {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        return buffer.toString();
    }

    public static void setCookie(String key, String value) {
        Cookie cookie = new Cookie(key, URLEncoder.encode(value, StandardCharsets.UTF_8));
        if (value.isEmpty()) {
            cookie.setMaxAge(0);
        } else {
            cookie.setMaxAge(3600 * 6);
        }
        cookie.setPath("/");
        var attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        attributes.getResponse().addCookie(cookie);
    }

    public static String getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public static String saveImageToStorage(String folder, MultipartFile imageFile) throws Exception {
        String uploadDirectory = "uploads/";
        if (folder != null) {
            uploadDirectory = uploadDirectory + folder + "/";
        }
        String uniqueFileName = getDate(new Date(), "yyyy-MM-dd") + "__" + UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
        ;
        Path uploadPath = Path.of(uploadDirectory + getDate(new Date(), "yyyy/MM/dd"));
        Path filePath = uploadPath.resolve(uniqueFileName);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFileName;
    }

    // Delete an image
    public static String deleteImage(String imageName) throws IOException {
        Path imagePath = Path.of("/" + imageName);

        if (Files.exists(imagePath)) {
            Files.delete(imagePath);
            return "Success";
        } else {
            return "Failed"; // Handle missing images
        }
    }

    public static void setSession(HttpServletRequest request, String key, String value) {
        HttpSession session = request.getSession();
        session.setAttribute(key, value);
    }

    public static Object getSession(HttpServletRequest request, String name) {
        HttpSession session = request.getSession();
        return request.getAttribute(name);
    }

    public static ArrayList<String> getListDayAndMonth() {
        ArrayList<String> days = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        Integer month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        int year = Year.now().getValue();
        for (int i = 1; i < maxDay; i++) {
            if (i < 10) {
                days.add(year + "-" + month + "-0" + i);
            } else {
                days.add(year + "-" + month + "-" + i);
            }
        }
        return days;
    }
}
