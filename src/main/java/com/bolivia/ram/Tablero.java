package com.bolivia.ram;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

/**
 * @web http://www.jc-mouse.net/
 * @author Mouse
 */
public class Tablero extends JPanel {
	// array con los nombres de las banderas 8 en total para 16 pares
	private Map<String, String> tablas;

	private int fila = 2;
	private int col = 10;
	private int ancho_casilla = 100;

	public boolean play = false;

	int c = 0;
	Casilla c1;
	Casilla c2;
	int aciertos = 0;

	/** Constructor de clase */
	public Tablero() {
		super();
		// propiedades
		setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		setLayout(new java.awt.GridLayout(fila, col));
		Dimension d = new Dimension((ancho_casilla * col), (ancho_casilla * fila));
		setSize(d);
		setPreferredSize(d);
		// crea instancias de casillas para crear el tablero
		String seleccion = (String) JOptionPane.showInputDialog(
				   null,
				   "Seleccione la tabla de multiplicar con la que quieres jugar",
				   "Memorama de tablas de multiplicar",
				   JOptionPane.QUESTION_MESSAGE,
				   null,  // null para icono defecto
				   new Object[] { "Tabla del 2", "Tabla del 3" }, 
				   "Tabla del 2");
		System.out.println(seleccion);
		tablas = new HashMap<>();
		if(seleccion==null) {
			 System.exit(0);
		}
		switch(seleccion) {

		case "Tabla del 2":
			tablas.put("2x1", "2");
			tablas.put("2x2", "4");
			tablas.put("2x3", "6");
			tablas.put("2x4", "8");
			tablas.put("2x5", "10");
			tablas.put("2x6", "12");
			tablas.put("2x7", "14");
			tablas.put("2x8", "16");
			tablas.put("2x9", "18");
			tablas.put("2x10", "20");
			break;
		case "Tabla del 3":
			tablas.put("3x1", "3");
			tablas.put("3x2", "6");
			tablas.put("3x3", "9");
			tablas.put("3x4", "12");
			tablas.put("3x5", "15");
			tablas.put("3x6", "18");
			tablas.put("3x7", "21");
			tablas.put("3x8", "24");
			tablas.put("3x9", "27");
			tablas.put("3x10", "30");
			break;
		}
		
		
		


		int count = 0;
		for (Entry<String, String> tabla : tablas.entrySet()) {
			// Casilla Operación
			Casilla op = new Casilla(String.valueOf(count++));
			op.setBandera(tabla.getKey());
			op.showBandera();
			op.addMouseListener(new juegoMouseListener());
			String tablaDel = tabla.getKey().substring(0, tabla.getKey().indexOf("x"));
			System.out.println("tabla del:" + tablaDel);
			op.setTablaDel(tablaDel);
			this.add(op);
			System.out.println(tabla.getKey());

			// Casilla Resultado
			Casilla rs = new Casilla(String.valueOf(count++));
			rs.setBandera(tabla.getValue());
			rs.showBandera();
			rs.setTablaDel(tablaDel);
			rs.addMouseListener(new juegoMouseListener());
			this.add(rs);
			System.out.println(tabla.getValue());

		}

		setVisible(true);
	}

	/**
	 * Inicia juegos - llena las casillas con pares de banderas
	 * 
	 * @return no tiene
	 */
	public void comenzarJuego() {
		aciertos = 0;
		play = true;
		Component[] componentes = this.getComponents();
		Set<Integer> indexs = new HashSet<>();

		for (int i = 0; i < componentes.length; i++) {
			((Casilla) componentes[i]).congelarImagen(false);
			((Casilla) componentes[i]).ocultarBandera();
			int n = (int) (Math.random() * (componentes.length));
			if (!indexs.contains(n)) {
				String banderaAux = ((Casilla) componentes[i]).getNameBandera();
				((Casilla) componentes[i]).setBandera(((Casilla) componentes[n]).getNameBandera());
				((Casilla) componentes[n]).setBandera(banderaAux);
				indexs.add(n);
			} else {
				i--;
			}

		}

	}

	/**
	 * Clase que implemenenta un MouseListener para la captura de eventos del mouse
	 */
	class juegoMouseListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {

			if (play) {
				c++;// lleva la cuenta de los click realizados en las casillas
				if (c == 1) { // primer click
					c1 = ((Casilla) e.getSource()); // obtiene objeto
					if (!c1.isCongelado()) {
						c1.showBandera();
						System.out.println("Primera Bandera: " + c1.getNameBandera());
					} else {// no toma en cuenta
						c = 0;
					}
				} else if (c == 2 && !c1.getName().equals(((Casilla) e.getSource()).getName())) {// segundo click
					c2 = ((Casilla) e.getSource());
					if (!c2.isCongelado()) {
						c2.showBandera();
						System.out.println("Segunda Bandera: " + c2.getNameBandera());
						// compara imagenes
						Animacion ani = new Animacion(c1, c2);
						ani.execute();
					}
					c = 0;// contador de click a 0
				} else { // mas de 2 clic consecutivos no toma en cuenta
					c = 0;
				}
			} else {
				int seleccion = JOptionPane.showOptionDialog(
						   null,
						   "Memorama de tablas de multiplicar", 
						   "Empezar a jugar",
						   JOptionPane.YES_NO_CANCEL_OPTION,
						   JOptionPane.QUESTION_MESSAGE,
						   null,    // null para icono por defecto.
						   new Object[] { "Comenzar a jugar"},   // null para YES, NO y CANCEL
						   "Comenzar a jugar");
				if(seleccion==0) {
					comenzarJuego();
				}
			}

		}

		@Override
		public void mousePressed(MouseEvent e) {
		}

		@Override
		public void mouseReleased(MouseEvent e) {
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}

	}

	/**
	 * 
	 */
	class Animacion extends SwingWorker<Void, Void> {
		private Casilla casilla1;
		private Casilla casilla2;

		public Animacion(Casilla value1, Casilla value2) {
			this.casilla1 = value1;
			this.casilla2 = value2;
		}

		@Override
		protected Void doInBackground() throws Exception {
			System.out.println("doInBackground: procesando imagenes...");
			// espera 1 segundo
			Thread.sleep(1000);
			String resultado = tablas.get(casilla1.getNameBandera());
			if(resultado==null) {
				resultado = tablas.get(casilla2.getNameBandera());
			}
			if (resultado!=null && (resultado.equals(casilla2.getNameBandera())||resultado.equals(casilla1.getNameBandera()))) {// son iguales
				casilla1.congelarImagen(true);
				casilla2.congelarImagen(true);
				System.out.println("doInBackground: imagenes son iguales");
				aciertos++;
				if (aciertos == getComponentCount()/2) {// win
					System.out.println("doInBackground: Usted es un ganador!");
					JOptionPane.showMessageDialog(null, "Usted es un ganador!");
				}
			} else {// no son iguales
				casilla1.ocultarBandera();
				casilla2.ocultarBandera();
				System.out.println("doInBackground: imagenes no son iguales");
			}
			return null;
		}

	}

}
