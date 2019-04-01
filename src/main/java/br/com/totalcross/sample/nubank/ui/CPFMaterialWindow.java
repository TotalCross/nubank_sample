package br.com.totalcross.sample.nubank.ui;

import java.sql.SQLException;

import br.com.totalcross.sample.nubank.dao.CPFDAO;
import br.com.totalcross.sample.nubank.util.Fonts;
import totalcross.ui.Button;
import totalcross.ui.Container;
import totalcross.ui.Edit;
import totalcross.ui.Label;
import totalcross.ui.MaterialWindow;
import totalcross.ui.Presenter;
import totalcross.ui.dialog.MessageBox;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.PressListener;
import totalcross.ui.gfx.Color;
import totalcross.util.InvalidDateException;

public class CPFMaterialWindow extends MaterialWindow {

	private static Edit maskedEdit;
	private static Button btnOutlined;

	public CPFMaterialWindow() {
		super(false, new Presenter() {
			@Override
			public Container getView() {
				return new Container() {
					@Override
					public void initUI() {
						Label cpfLabel = new Label("Para acessar o app digite o \nseu CPF:");
						cpfLabel.setFont(Fonts.latoLightPlus6);

						add(cpfLabel, LEFT + 100, AFTER + 50, PREFERRED, Inicial.PREFERRED);

						maskedEdit = new Edit("999.999.999-99");
						btnOutlined = new Button("Continuar", Button.BORDER_OUTLINED);

						maskedEdit.caption = "";
						maskedEdit.setMode(Edit.NORMAL, true);
						maskedEdit.setValidChars(Edit.numbersSet);
						maskedEdit.transparentBackground = true;
						maskedEdit.addPressListener(new PressListener() {

							@Override
							public void controlPressed(ControlEvent event) {
								if (event.target == maskedEdit
										&& maskedEdit.getText().length() == "999.999.999-99".length()) {
									btnOutlined.setEnabled(true);
									btnOutlined.setForeColor(Color.BLUE);
									btnOutlined.repaintNow();
								} else {
									btnOutlined.setEnabled(false);
									btnOutlined.setForeColor(Color.getRGB(204, 204, 204));
									btnOutlined.repaintNow();
								}

							}
						});

						btnOutlined.setEnabled(false);

						btnOutlined.setBackForeColors(Color.getRGB(204, 204, 204), Color.WHITE);

						add(maskedEdit, SAME, AFTER + 50, PREFERRED, Inicial.PREFERRED);
						add(btnOutlined, LEFT, AFTER + 100, FILL, PREFERRED);
						btnOutlined.addPressListener((event) -> {
							try {
								doInsert();
							} catch (Exception ee) {
								MessageBox.showException(ee, true);
							}
						});
					}
				};
			}
		});

		transparentBackground = true;
		fadeOtherWindows = true;

		this.setBackColor(Color.getRGB(108, 37, 117));
		this.setSlackSpace(100);
	}

	private static void doInsert() throws SQLException, InvalidDateException {
		if (maskedEdit.getTextWithoutMask() == "") {
			MessageBox mb = new MessageBox("Atenção!", "Preencha o campo CPF");
			mb.setBackForeColors(Color.WHITE, Color.BLACK);
			mb.popup();

		} else {

			// simple example of how you can insert data into SQLite..
			String cpf = maskedEdit.getTextWithoutMask();
			new CPFDAO().insertCPF(cpf);

			MessageBox mb = new MessageBox("Atenção!", "CPF:" + cpf + " foi cadastrado com sucesso!");
			mb.setBackForeColors(Color.WHITE, Color.BLACK);
			mb.popup();
		}
	}
}
