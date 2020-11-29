package com.qrequest.control;

import com.qrequest.objects.Post;

/**Class for deleting questions from the database.
 */
public abstract class DeletePostControl {

	/**Sends a question to the DataManager to be removed from the database.
	 * @param question The question being removed.
	 */
	public static void processDeletePost(Post post) {
		DataManager.deletePost(post);
	}
} 