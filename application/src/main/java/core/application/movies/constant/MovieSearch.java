package core.application.movies.constant;

public enum MovieSearch {
	RANK("RANK,1"),
	LATEST("prodYear,1");

	public final String SORT;

	MovieSearch(String sort) {
		this.SORT = sort;
	}
}
