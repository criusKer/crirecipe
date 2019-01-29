package crirecipe.criusker.crirecipe.entity;

public class ResponseObject<T> {
	private String msg;
	private int state = 1;
	private T datas;
	private int page;
	private int count;
	private int size;
	
	public ResponseObject(int state, String msg){
		this.state=state;
		this.msg=msg;
		
	}
	public ResponseObject(int state, T datas){
		this.state=state;
		this.datas=datas;
	}
	public ResponseObject(int state, String msg, T datas){
		this.state=state;
		this.msg = msg;
		this.datas=datas;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public Object getDatas() {
		return datas;
	}
	public void setDatas(T datas) {
		this.datas = datas;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
}
