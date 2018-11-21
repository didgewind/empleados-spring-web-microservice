package profe.empleados.web.model;

import java.util.ArrayList;
import java.util.List;

public class Departamento {

	private String id;
	private String desc;
	private List<Empleado> empleados = new ArrayList<Empleado>();
	
	public Departamento() {
		// TODO Auto-generated constructor stub
	}

	public Departamento(String id, String desc) {
		super();
		this.id = id;
		this.desc = desc;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public List<Empleado> getEmpleados() {
		return empleados;
	}

}
