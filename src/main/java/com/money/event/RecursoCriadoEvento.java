package com.money.event;

import org.springframework.context.ApplicationEvent;

import javax.servlet.http.HttpServletResponse;

public class RecursoCriadoEvento extends ApplicationEvent {
    private static long serialVersionUID = 1l;
    private HttpServletResponse response;
    private Long codigo;
    public RecursoCriadoEvento(Object source, HttpServletResponse response, Long codigo) {
        super(source);
        this.response = response;
        this.codigo = codigo;
    }
    public HttpServletResponse getResponse() {
        return response;
    }
    public Long getCodigo() {
        return codigo;
    }
}
