package com.esie.core.ioc.parents;

import com.esie.core.ui.Element;

public interface WindowInterface {
    void close();
    void construct(Element element);
    void destruct(Element element);
}
