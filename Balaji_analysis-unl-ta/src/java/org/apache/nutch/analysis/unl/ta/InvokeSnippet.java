package org.apache.nutch.analysis.unl.ta;
//package org.apache.nutch.snippetgeneration.unl.ta;

import org.apache.nutch.snippetgeneration.Snippetgenerator;

public class InvokeSnippet implements Snippetgenerator {

    SnippetGet snippetTest = new SnippetGet();
    //PlaceListget placeListget=new PlaceListget();

public String[] snippet_generator(String constraints, String docid, String url, String tamilWord) {
	////System.out.println("constrains for snippet"+constraints+"\t"+docid+"\t"+url+"\t"+tamilWord);
        String[] snippet=new String[4];
        String[] Snippets = new String[3];
        Snippets[0] = "";
        Snippets[1] = "";
        Snippets[2] = "";

        if (!constraints.contains("is empty")) {
            String[] spt = constraints.split(":");
            for (int i = 0; i < spt.length; i++) {
             
                String[] getsnippet = snippetTest.getSnippet("d" + docid.trim(), spt[i].trim());
		//System.out.println("getSnippet"+getsnippet);
                if (i == 0) {
                    Snippets[0] = getsnippet[0];
                }
                Snippets[1] = Snippets[1] + getsnippet[1] + " ";
                Snippets[2] = Snippets[2] + getsnippet[2] + ", ";
            }
            snippet[0] = snippetArrangements(Snippets, url, docid, tamilWord);
            //snippet[1]=placeListget.getPlaceList("d"+docid);
	    //snippet[3]=new SummaryGet().getEngSummaryunl("d"+docid);
	    System.out.println("snippet[2]"+snippet[0].toString());	
		//System.out.println("snippet[0]"+snippet[0].toString());
		//System.out.println("snippet[1]"+snippet[1].toString());
            return snippet;
        }
        else{
            snippet[0]="<h2>No Match Found</h2>";
            return snippet;
        }
    }

    public String snippetArrangements(String[] input, String url, String docid, String tamilWord) {
        SummaryGet summary1 = new SummaryGet();

        String[] urlsplit = url.split("/");
        String urlShort = urlsplit[0] + "/" + urlsplit[1] + "/" + urlsplit[2] + "/";
        String output = "<p style=\"margin-right:30%\"><u><a href=" + url + ">" + input[0] + ".......</a></u><br/>" + "<a style=\"color:gray\" href=" + url + ">" + urlShort + "</a> " + "<font size=\"1.5\" color=\"green\">" + docid + "--->" + input[2] + "</font><br/>" + "...." + input[1]  + summary1.getSummaryunl("d" + docid) + "</br>"+"</p>";
System.out.println("subaink"+output);
        return output;
    }

	public String rating(String docid)
	{
		StringBuffer sbresult=new StringBuffer();
		
		if(docid!=null)
		{
			
			//sbresult.append("<jsp:include page=\"rating.html\">");
			//System.out.println("getting rating page for"+docid);
		/*sbresult.append("<a href=\"javascript:loadStars();\"<img src=\"star1.gif\"onmouseover=\"javascript:highlight(docid)\"onclick=\"javascript:setStar(docid)\"onmouseout=\"javascript:losehighlight(docid)\"id=\"1\"style=\"width:30px;height:30px;float:left;\"/>");
		sbresult.append("<img src=\"star1.gif\"onmouseover=\"javascript:highlight(docid)\"onclick=\"javascript:setStar(docid)\"onmouseout=\"javascript:losehighlight(docid)\"id=\"2\"style=\"width:30px;height:30px;float:left;\"/>");	
		sbresult.append("<img src=\"star1.gif\"onmouseover=\"javascript:highlight(docid)\"onclick=\"javascript:setStar(docid)\"onmouseout=\"javascript:losehighlight(docid)\"id=\"3\"style=\"width:30px;height:30px;float:left;\"/>");
		sbresult.append("<img src=\"star1.gif\"onmouseover=\"javascript:highlight(docid)\"onclick=\"javascript:setStar(docid)\"onmouseout=\"javascript:losehighlight(docid)\"id=\"4\"style=\"width:30px;height:30px;float:left;\"/>");
	sbresult.append("<img src=\"star1.gif\"onmouseover=\"javascript:highlight(docid)\"onclick=\"javascript:setStar(docid)\"onmouseout=\"javascript:losehighlight(docid)\"id=\"5\"style=\"width:30px;height:30px;float:left;\"/>"+"</a>");
			sbresult.append("<div id=\"vote\"style=\"font-family:tahoma;color:red;\"/>"+"</div>");*/
		}
		return sbresult.toString();
	}
    public String lengthCheck(String input) {
        String output = "";
        String[] hitsspt = input.split(" ");
        String cutlist = "";
        for (String s : hitsspt) {
            int start = 0, end = 25;
            if (s.length() > 50) {
                while (start != end) {
                    if (end < s.length()) {
                        cutlist = cutlist + s.substring(start, end) + " ";
                        end += 25;
                        start += 25;
                    } else {
                        end = end - 25;
                        if (end < s.length()) {
                            cutlist = cutlist + s.substring(end, s.length() - 1) + " ";
                        }
                        start = end;
                        s = cutlist;
                    }
                }
            }
            output = output + s + " ";
        }
        return output;
    }

    public String Highlightwords(String input, String highligthword, String color) {
        String[] spt = highligthword.split(" ");
        for (String s : spt) {
            if (input.contains(s)) {
                input = input.replaceAll(s, "<b><font color=\"" + color + "\">" + s + "</font></b>");
            }
        }
        return input;
    }
}

