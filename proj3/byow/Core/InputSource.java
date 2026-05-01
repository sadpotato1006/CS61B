package byow.Core;

public interface InputSource {

    boolean hasNextKey();

    char getNextKey();

    // 这个输入源是否已经彻底结束
    boolean isExhausted();

}
