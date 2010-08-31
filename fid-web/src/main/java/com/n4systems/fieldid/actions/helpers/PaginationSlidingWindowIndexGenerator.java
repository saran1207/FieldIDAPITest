package com.n4systems.fieldid.actions.helpers;

import java.util.ArrayList;
import java.util.List;

public class PaginationSlidingWindowIndexGenerator {
	

	public static List<Integer> generateWindowPageNumberList(int page, int totalPages, int blockSize, int startingIndex) {
		// round our current page down to the nearest PAGE_BLOCK_SIZE (eg 77 -> 70) and reindex to INDEX_START
		
		int startOffset = ((int)Math.floor( blockSize / 2) - 1 );
		int endOffset = blockSize - 1;
		
		int startingPage =  page - startOffset;
		if (startingPage < startingIndex ) { startingPage = startingIndex; }
		
		if (startingPage + endOffset > page) { startingPage = totalPages - endOffset; }
		
		if (startingPage < startingIndex) { startingPage = startingIndex;}
		
		int endingPage = startingPage + endOffset;
		if (endingPage > totalPages) { endingPage=totalPages; }
		
		
		List<Integer> pageList = new ArrayList<Integer>();
		for (int i = startingPage; i <= endingPage; i++) {
			pageList.add(i);
		}
		
		return pageList;
	}
	
	
}