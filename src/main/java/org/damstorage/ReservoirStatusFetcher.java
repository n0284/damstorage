package org.damstorage;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReservoirStatusFetcher {

    public static class DamStatus {
        public final String name;
        public final String rate;

        public DamStatus(String name, String rate) {
            this.name = name;
            this.rate = rate;
        }
    }

    public List<DamStatus> fetch() throws IOException {
        String url = "https://www.waterworks.metro.tokyo.lg.jp/suigen/suigen";
        //String email = System.getenv("NOTIFY_EMAIL");
        //String userAgent = String.format("damstorage/1.0 (+mailto:%s)", email);
        Document doc = Jsoup.connect(url)
                //.userAgent(userAgent)
                .get();

        Element damTable = doc.selectFirst("table");
        if (damTable == null) return List.of();

        List<DamStatus> result = new ArrayList<>();
        List<String> targets = List.of("矢木沢ダム", "八ッ場ダム");

        for (Element row : damTable.select("tr")) {
            Elements cells = row.select("td");
            if (cells.size() < 6) continue;

            String damName = cells.get(0).text();
            for (String target : targets) {
                if (damName.contains(target)) {
                    String rate = cells.get(6).text();
                    result.add(new DamStatus(damName, rate));
                    break;
                }
            }
        }

        return result;
    }
}