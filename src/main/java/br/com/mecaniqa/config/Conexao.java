package br.com.mecaniqa.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
    private Conexao() {
    }

//Tem que adicionar variável nos dados sensíveis, usuário e senha!!!
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/mecaniqa";
        String usuario = "root";
        String senha = "200905Ke.";

        if (url == null || usuario == null || senha == null) {
            throw new SQLException(
                "Configure a URL, o usuário e a senha do banco");
        }

        return DriverManager.getConnection(url, usuario, senha);
    }
}
