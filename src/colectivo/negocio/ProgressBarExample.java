package colectivo.negocio;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

public class ProgressBarExample {

    private JFrame frame;
    private JProgressBar progressBar;

    public ProgressBarExample() {
        frame = new JFrame("Barra de Progreso");
        progressBar = new JProgressBar(0, 100);

        frame.setLayout(null);
        progressBar.setBounds(10, 10, 300, 30);

        frame.add(progressBar);

        frame.setSize(330, 100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Iniciar el hilo de simulación
        simulateProgress();
    }

    private void simulateProgress() {
        Thread thread = new Thread(() -> {
            for (int i = 0; i <= 100; i++) {
                final int progressValue = i;
                SwingUtilities.invokeLater(() -> {
                    // Actualizar el valor de la barra de progreso en el hilo de la interfaz de usuario
                    progressBar.setValue(progressValue);
                });

                try {
                    // Simular trabajo en segundo plano con Thread.sleep y un valor aleatorio
                    Thread.sleep((long) (Math.random() * 200));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ProgressBarExample::new);
    }
}
