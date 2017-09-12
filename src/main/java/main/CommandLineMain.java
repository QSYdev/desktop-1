package main;

import libterminal.api.TerminalAPI;

import java.net.Inet4Address;
import java.util.Scanner;

public class CommandLineMain {

	public static void main(String[] args) throws Exception {
		TerminalAPI term = new TerminalAPI((Inet4Address) Inet4Address.getByName(System.getenv("MY_IP")));
	    boolean up = false;

		System.out.println("Comandos:");
		System.out.println("s: Iniciar terminal.");
		System.out.println("p: Terminar terminal.");
		System.out.println("a: Iniciar busqueda de nodos");
		System.out.println("x: Detener busqueda de nodos");
		System.out.println("n: Obtener cantidad de nodos");
		System.out.println("q: Salir");

		Scanner sc = new Scanner(System.in);
		char option = 0;

		while(option != 'q') {
			System.out.print("\nComando: ");
			option = sc.next().trim().charAt(0);
			
			switch (option) {
			case 's':
				if(up) {
					System.out.println("Terminal ya esta corriendo.");
					break;
				}
				term.start();
				up = true;
				System.out.println("Terminal iniciada.");
				break;
			case 'p':
			case 'q':
				if (!up) {
					System.out.println("Terminal no esta corriendo.");
					break;
				}
				term.stop();
				up = false;
				System.out.println("Terminal terminada.");
				break;
			case 'a':
				if (!up) {
					System.out.println("Terminal no esta corriendo.");
					break;
				}
				term.startNodesSearch();
				System.out.println("Búsqueda de nodos iniciada.");
				break;
			case 'x':
				if (!up) {
					System.out.println("Terminal no esta corriendo.");
					break;
				}
				term.stopNodesSearch();
				System.out.println("Busqueda detenida.");
				break;
				case 'n':
					if(!up) {
						System.out.println("Terminal no esta corriendo.");
						break;
					}
					System.out.println("Cantidad de nodos conectados: "+term.connectedNodesAmount());
					break;
			default:
				break;
			}	
		}
		sc.close();
		System.out.print("Buh-bye");
	}
}
