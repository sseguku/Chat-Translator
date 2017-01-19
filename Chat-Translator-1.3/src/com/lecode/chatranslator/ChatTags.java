package com.lecode.chatranslator;

public class ChatTags {
	int _id;
	String _left_user;
	String _right_machine;
	

	public ChatTags() {
		
	}
	public ChatTags(int id, String _left_user, String _right_machine) {
		this._id = id;
		this._left_user = _left_user;
		this._right_machine = _right_machine;
	}
	public ChatTags( String _left_user, String _right_machine) {

		this._left_user = _left_user;
		this._right_machine = _right_machine;
	}
	/**
	 * @return the _id
	 */
	public int get_id() {
		return _id;
	}
	/**
	 * @param _id the _id to set
	 */
	public void set_id(int _id) {
		this._id = _id;
	}
	/**
	 * @return the _left_user
	 */
	public String get_left_user() {
		return _left_user;
	}
	/**
	 * @param _left_user the _left_user to set
	 */
	public void set_left_user(String _left_user) {
		this._left_user = _left_user;
	}
	/**
	 * @return the _right_machine
	 */
	public String get_right_machine() {
		return _right_machine;
	}
	/**
	 * @param _right_machine the _right_machine to set
	 */
	public void set_right_machine(String _right_machine) {
		this._right_machine = _right_machine;
	}
}
