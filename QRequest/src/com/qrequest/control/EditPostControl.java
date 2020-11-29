package com.qrequest.control;

import com.qrequest.objects.Post;

/**Class for editing questions in the database.
 */
public abstract class EditPostControl {

	/**Sends a question to the DataManager to be edited from the database.
	 * @param question The question being edited.
	 */
	public static void processEditPost(Post post) {
		DataManager.editPost(post);
	}
} 