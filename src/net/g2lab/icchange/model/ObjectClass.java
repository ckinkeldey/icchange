package net.g2lab.icchange.model;

public class ObjectClass {

	static int allIds = 0;

	int id;
	String label;
	int classcode = -1;

	public ObjectClass() {
		this("");
	}
	
	public ObjectClass(String label) {
		this(allIds++, label);
	}

	public ObjectClass(int id, String label) {
		this(allIds++, label, -1);
	}
	
	public ObjectClass(int id, String label, int classcode) {
		this.label = label;
		this.id = id;
		this.classcode = classcode;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label
	 *            the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	
	
	/**
	 * @return the classcode
	 */
	public int getClasscode() {
		return classcode;
	}

	/**
	 * @param classcode the classcode to set
	 */
	public void setClasscode(int classcode) {
		this.classcode = classcode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ObjectClass other = (ObjectClass) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return label + "";
	}



}
