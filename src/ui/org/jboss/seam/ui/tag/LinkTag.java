package org.jboss.seam.ui.tag;

import javax.faces.component.UIComponent;

import org.jboss.seam.ui.HtmlLink;


public class LinkTag extends HtmlCommandLinkTagBase
{
    public String getComponentType()
    {
        return HtmlLink.COMPONENT_TYPE;
    }

    public String getRendererType()
    {
        return null;
    }

    private String view;
    private String action;
    private String buttonClass;
    private String linkStyle;
    private String propagation;
    private String pageflow;
    private String taskInstance;
    private String fragment;

    protected void setProperties(UIComponent component)
    {
        super.setProperties(component);
        setStringProperty(component, "view", view);
        setStringProperty(component, "action", action);
        setValueBinding(component, "taskInstance", taskInstance);
        setStringProperty(component, "buttonClass", buttonClass);
        setStringProperty(component, "linkStyle", linkStyle);
        setStringProperty(component, "propagation", propagation);
        setStringProperty(component, "pageflow", pageflow);
        setStringProperty(component, "fragment", fragment);
    }

    public void setAction(String action)
    {
        this.action = action;
    }

   public void setButtonClass(String buttonClass)
   {
      this.buttonClass = buttonClass;
   }

   public void setLinkStyle(String linkStyle)
   {
      this.linkStyle = linkStyle;
   }

   public void setPageflow(String pageflow)
   {
      this.pageflow = pageflow;
   }

   public void setPropagation(String propagation)
   {
      this.propagation = propagation;
   }

   public void setView(String view)
   {
      this.view = view;
   }

   public void setTaskInstance(String task)
   {
      this.taskInstance = task;
   }

   public void setFragment(String fragment)
   {
      this.fragment = fragment;
   }
}
