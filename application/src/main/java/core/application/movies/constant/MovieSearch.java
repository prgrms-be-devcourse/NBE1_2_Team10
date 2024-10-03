package core.application.movies.constant;

public enum MovieSearch {
	RANK("RANK,1"),
	LATEST("prodYear,1");

	public final String SORT;

	MovieSearch(String sort) {
		this.SORT = sort;
	}

	public String getSORT() {
		return SORT;
	}

	public static boolean isNotValid(String sort) {
		for (MovieSearch value : MovieSearch.values()) {
			if (value.name().equals(sort)) {
				return false;
			}
		}
		return true;
	}
}
