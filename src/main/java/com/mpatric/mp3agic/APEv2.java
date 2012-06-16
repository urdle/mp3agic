package com.mpatric.mp3agic;

import java.util.Map;

public interface APEv2 {
	Map<String, APEv2TagItem> getFrameSets();
	byte[] toBytes() throws NotSupportedException;

}
