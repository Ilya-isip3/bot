package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;

public class MyAmazingBot extends TelegramLongPollingBot {

    private static final String[][] GAME_BOARD = new String[3][3];

    private static final String X = "X";
    private static final String O = "O";
    private static final String EMPTY = "";

    private static boolean isPlayer1Turn = true;

    public static void main(String[] args) {
        // ...
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            String text = message.getText();

            if (text.equals("/start")) {
                // Start the game
                initializeGameBoard();
                sendGameBoard(message.getChatId());
            } else if (text.length() == 2 && text.matches("[1-3]{2}")) {
                // Player's move
                int row = Integer.parseInt(text.substring(0, 1)) - 1;
                int col = Integer.parseInt(text.substring(1)) - 1;

                if (isMoveValid(row, col)) {
                    makeMove(row, col, isPlayer1Turn ? X : O);
                    isPlayer1Turn = !isPlayer1Turn;

                    sendGameBoard(message.getChatId());

                    checkGameState(message.getChatId());
                } else {
                    sendInvalidMoveMessage(message.getChatId());
                }
            } else {
                // Invalid command
                sendInvalidCommandMessage(message.getChatId());
            }
        }
    }

    private void initializeGameBoard() {
        for (String[] row : GAME_BOARD) {
            Arrays.fill(row, EMPTY);
        }
    }

    private void sendGameBoard(long chatId) {
        StringBuilder gameBoardMessage = new StringBuilder();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (j > 0) {
                    gameBoardMessage.append("|");
                }
                gameBoardMessage.append(GAME_BOARD[i][j]);
            }
            if (i < 2) {
                gameBoardMessage.append("\n─┼─┼─\n");
            }
        }
        sendText(chatId, gameBoardMessage.toString());
    }

    private boolean isMoveValid(int row, int col) {
        return row >= 0 && row < 3 && col >= 0 && col < 3 && GAME_BOARD[row][col].equals(EMPTY);
    }

    private void makeMove(int row, int col, String player) {
        GAME_BOARD[row][col] = player;
    }

    private void checkGameState(long chatId) {
        String winner = getWinner();

        if (winner != null) {
            // Game over
            sendGameOverMessage(chatId, winner);
        } else if (isTie()) {
            // Tie game
            sendTieMessage(chatId);
        }
    }

    private String getWinner() {
        // Check rows
        for (String[] row : GAME_BOARD) {
            if (row[0].equals(row[1]) && row[1].equals(row[2]) && !row[0].equals(EMPTY)) {
                return row[0];
            }
        }

        // Check columns
        for (int i = 0; i < 3; i++) {
            if (GAME_BOARD[0][i].equals(GAME_BOARD[1][i]) && GAME_BOARD[1][i].equals(GAME_BOARD[2][i]) && !GAME_BOARD[0][i].equals(EMPTY)) {return GAME_BOARD[0][i];
            }
        }

        // Check diagonals
        if (GAME_BOARD[0][0].equals(GAME_BOARD[1][1]) && GAME_BOARD[1][1].equals(GAME_BOARD[2][2]) && !GAME_BOARD[0][0].equals(EMPTY)) {
            return GAME_BOARD[0][0];
        } else if (GAME_BOARD[0][2].equals(GAME_BOARD[1][1]) && GAME_BOARD[1][1].equals(GAME_BOARD[2][0]) && !GAME_BOARD[0][2].equals(EMPTY)) {
            return GAME_BOARD[0][2];
        }

        return null;
    }

    private boolean isTie() {
        for (String[] row : GAME_BOARD) {
            for (String cell : row) {
                if (cell.equals(EMPTY)) {
                    return false;
                }
            }
        }

        return true;
    }

    private void sendGameOverMessage(long chatId, String winner) {
        String message = winner + " выиграл игру!";
            sendText(chatId, message);
    }

    private void sendTieMessage(long chatId) {
        String message = "Ничья!";

        sendText(chatId, message);
    }

    private void sendInvalidMoveMessage(long chatId) {
        String message = "Недопустимый ход.";

        sendText(chatId, message);
    }

    private void sendInvalidCommandMessage(long chatId) {
        String message = "Неверная команда. Пожалуйста, введите /start, чтобы начать новую игру!";

        sendText(chatId, message);
    }

    @Override
    public String getBotUsername() {
        return "javaBot2000_bot";
    }

    @Override
    public String getBotToken() {
        return "7027398943:AAFVZ4k9jQJ_QHSFl-XZajJLGm14I4y66Po";
    }
    public void sendText(Long who, String what){
        SendMessage sm = new SendMessage();
        sm.setChatId(who.toString());
        sm.setText(what);
        try {
            execute(sm);                        //Actually sending the message
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);      //Любая ошибка будет выведена здесь
        }
    }
}