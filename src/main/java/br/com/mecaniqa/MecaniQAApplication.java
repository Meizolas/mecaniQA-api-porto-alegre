package br.com.mecaniqa;

import java.util.Scanner;

public class MecaniQAApplication {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice = 0;

        do {
            System.out.println("\n--- MecâniQA Gestão ---");
            System.out.println("1. Adicionar Peça");
            System.out.println("2. Listar Peças");
            System.out.println("3. Atualizar Peça");
            System.out.println("4. Remover Peça");
            System.out.println("5. Buscar Peça por Código");
            System.out.println("-----------------------");
            System.out.println("6. Adicionar Serviço");
            System.out.println("7. Listar Serviços");
            System.out.println("8. Atualizar Serviço");
            System.out.println("9. Remover Serviço");
            System.out.println("10. Buscar Serviço por Código");
            System.out.println("-----------------------");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Opção inválida. Por favor, insira um número.");
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.println("Opção 1 selecionada: Adicionar Peça");
                    // Lógica para adicionar peça
                    break;
                case 2:
                    System.out.println("Opção 2 selecionada: Listar Peças");
                    // Lógica para listar peças
                    break;
                case 3:
                    System.out.println("Opção 3 selecionada: Atualizar Peça");
                    // Lógica para atualizar peça
                    break;
                case 4:
                    System.out.println("Opção 4 selecionada: Remover Peça");
                    // Lógica para remover peça
                    break;
                case 5:
                    System.out.println("Opção 5 selecionada: Buscar Peça por Código");
                    // Lógica para buscar peça
                    break;
                case 6:
                    System.out.println("Opção 6 selecionada: Adicionar Serviço");
                    // Lógica para adicionar serviço
                    break;
                case 7:
                    System.out.println("Opção 7 selecionada: Listar Serviços");
                    // Lógica para listar serviços
                    break;
                case 8:
                    System.out.println("Opção 8 selecionada: Atualizar Serviço");
                    // Lógica para atualizar serviço
                    break;
                case 9:
                    System.out.println("Opção 9 selecionada: Remover Serviço");
                    // Lógica para remover serviço
                    break;
                case 10:
                    System.out.println("Opção 10 selecionada: Buscar Serviço por Código");
                    // Lógica para buscar serviço
                    break;
                case 0:
                    System.out.println("Saindo do sistema...");
                    scanner.close();
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        } while(choice != 0);
    }
}