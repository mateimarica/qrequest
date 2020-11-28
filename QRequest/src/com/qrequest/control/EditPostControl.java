package com.qrequest.control;

import com.qrequest.objects.Post;

/**Class for deleting questions to the database.
 */
public abstract class EditPostControl {

	/**Sends a question to the DataManager to be removed from the database.
	 * @param question The question being removed.
	 */
	public static void processEditPost(Post post) {
		DataManager.editPost(post);
	}
} 