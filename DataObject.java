import java.io.Serializable;

public class DataObject implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String message;

	public DataObject() {
	}

	public DataObject(String message) {
		setMessage(message);
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
