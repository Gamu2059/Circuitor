package Sho.CopyTo;

/**
 * copyToメソッドの実装を確かにするためのインタフェース。
 *
 * @author 翔
 * @version 1.1
 */
public interface CopyTo {
    /**
     * 自身のフィールドを引数インスタンスに複製します。<br>
     * cloneメソッドとの違いは、こちらはインスタンスとして存在するものに複製するという点です。
     *
     * @param o 引数インスタンス
     * @exception CannotCopyToException 引数インスタンスが存在しない場合や、自身のクラス型にキャスト不可の場合
     * @see CannotCopyToException
     * @since 1.1
     */
    public void copyTo(Object o) throws CannotCopyToException;
}
