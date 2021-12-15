package org.example.preloader;

import java.util.List;

public interface DataPreloader<T> {

	/**
	 * Preloads data for the type T.
	 * @return
	 */
	List<T> preloadData();
}
