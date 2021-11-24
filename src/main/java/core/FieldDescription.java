
package core;

public class FieldDescription  implements AbstractField{
    private String field;
    private String type;
    private String _null;
    private String key;
    private Object _default;
    private String extra;

    public FieldDescription(String field, String type, String _null, String key, Object _default, String extra) {
        this.field = field;
        this.type = type;
        this._null = _null;
        this.key = key;
        this._default = _default;
        this.extra = extra;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNull() {
        return _null;
    }

    public void setNull(String _null) {
        this._null = _null;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getDefault() {
        return _default;
    }

    public void setDefault(String _default) {
        this._default = _default;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }
    public String toString(){
        return String.format("%s\n%s\n%s\n%s\n%s\n%s\n", getField(),getType(),getNull(),getKey(),getDefault(),getExtra());
    }
}
