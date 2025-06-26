package noodlezip.common.util;


public class ValidationUtil {

    /*
      문자열이 null이거나 공백일 경우 예외를 발생시킵니다.
     */
    public static void requireNonBlank(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
//            throw new CustomException(
//                    ErrorCode.INVALID_INPUT,
//                    fieldName + "은(는) 필수 입력 항목입니다."
//            );
        }
    }

    /*
      객체가 null일 경우 예외를 발생시킵니다.
     */
    public static void requireNonNull(Object value, String fieldName) {
        if (value == null) {
//            throw new CustomException(
//                    ErrorCode.INVALID_INPUT,
//                    fieldName + "이(가) 비어 있을 수 없습니다."
//            );
        }
    }

    /*
      문자열이 지정된 최소 길이보다 짧으면 예외를 발생시킵니다.
     */
    public static void requireMinLength(String value, int minLength, String fieldName) {
        if (value == null || value.length() < minLength) {
//            throw new CustomException(
//                    ErrorCode.INVALID_INPUT,
//                    fieldName + "은(는) 최소 " + minLength + "자 이상이어야 합니다."
//            );
        }
    }

    /*
      숫자가 지정된 최소값보다 작으면 예외를 발생시킵니다.
     */
    public static void requireMin(int value, int min, String fieldName) {
        if (value < min) {
//            throw new CustomException(
//                    ErrorCode.INVALID_INPUT,
//                    fieldName + "은(는) 최소값 " + min + " 이상이어야 합니다."
//            );
        }
    }
}