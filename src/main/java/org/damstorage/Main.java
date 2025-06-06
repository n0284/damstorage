package org.damstorage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        //String webhookUrl = "SLACK_WEBHOOK_URL";

        ReservoirStatusFetcher fetcher = new ReservoirStatusFetcher();
        SlackNotifier notifier = new SlackNotifier("SLACK_WEBHOOK_URL");

        try {
            List<ReservoirStatusFetcher.DamStatus> damStatuses = fetcher.fetch();

            if (damStatuses.isEmpty()) {
                System.out.println("ダムデータが取得できませんでした");
                return;
            }

            StringBuilder message = new StringBuilder();
            message.append("🌀ダムの貯水率（").append(LocalDate.now()).append("）\n");
            for (ReservoirStatusFetcher.DamStatus dam : damStatuses) {
                message.append(dam.name).append(": ").append(dam.rate).append("%\n");
            }

            notifier.sendMessage(message.toString());

        } catch (IOException e) {
            System.err.println("エラーが発生しました: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}