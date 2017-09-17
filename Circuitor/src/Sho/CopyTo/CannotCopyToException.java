package Sho.CopyTo;

/**
 * copyToメソッドに予期せぬ引数が与えられた場合に発生する例外。
 *
 * @author 翔
 * @version 1.1
 */
public class CannotCopyToException extends Exception {
    /**
     * @param className 自身のクラス名
     * @since 1.1
     */
    public CannotCopyToException(String className) {
        super("CannotCopyToException : " + className + "クラスのcopyToに予期せぬオブジェクトが渡されました。");
    }
}
