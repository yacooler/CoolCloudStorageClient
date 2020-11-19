package frames;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


/**
 * Базовый класс "Пакет", посредством которого передаются данные с клиента на сервер и обратно
 */


public abstract class BaseFrame implements Serializable {

    private static final long serialVersionUID = -4122241077920180396L;


    //Поле, если вдруг нужно будет реализовать бинарный протокол - 3хсимвольное обозначение команды
    protected byte[] semantic;

    @Setter @Getter
    protected int contentDataSize;

    protected byte[] content;


    public BaseFrame() {
        afterConstruct();
    }

    public BaseFrame(byte[] content) {
        this.content = content;
        System.out.println(new String(content));
        afterConstruct();
    }

    public BaseFrame(String content) {
        this.content = content.getBytes();
        System.out.println(new String(content));
        afterConstruct();
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public byte[] getContent(){
        return this.content;
    }

    public String getContentAsString(){
        return content != null ? new String(content) : "";
    }

    public void setContentAsString(String content){
        this.content = content.getBytes();
    }

    protected void afterConstruct(){}


}
