package com.ade.purifier.service.web.pages.sys;

import com.ade.purifier.orm.dao.SysUserDao;
import com.ade.purifier.orm.model.SysUser;
import com.ade.purifier.service.web.pages.BorderPage;
import org.apache.cayenne.ObjectContext;
import org.apache.click.ActionListener;
import org.apache.click.Control;
import org.apache.click.control.*;
import org.apache.click.dataprovider.DataProvider;
import org.apache.click.extras.control.LinkDecorator;
import org.apache.click.extras.control.TableInlinePaginator;

import java.util.List;

/**
 *
 * Created by ismeade on 2014/11/14.
 */
public class SysUserInfoPage extends BorderPage {

    /** */
    private static final long serialVersionUID = 1L;

    private Table table = new Table("table");

    private PageLink editLink = new PageLink("Edit", SysUserEditPage.class);

    private ActionLink deleteLink = new ActionLink("Delete", this, "onDeleteClick");

    private SysUserDao sysUserDao;

    public SysUserInfoPage() {
        sysUserDao = new SysUserDao((ObjectContext) getContext().getRequest().getSession().getAttribute("objectContext"));

        // Add controls
        addControl(table);
        addControl(editLink);
        addControl(deleteLink);

        PageLink pageLink = new PageLink("newObject", "新建", SysUserCreatePage.class);
        pageLink.setImageSrc("/images/table.png");
        addControl(pageLink);

        // Setup table
        table.setClass(Table.CLASS_SIMPLE);
        table.setPageSize(10);
        table.setShowBanner(true);
        table.setSortable(true);
        table.setPaginator(new TableInlinePaginator(table));
        table.setPaginatorAttachment(Table.PAGINATOR_INLINE);

        table.addColumn(new Column(SysUser.ID_PK_COLUMN, "ID"));
        table.addColumn(new Column(SysUser.USER_NAME.getName(), "用户名"));
        table.addColumn(new Column(SysUser.ROLE.getName(), "用户身份"));
        table.addColumn(new Column(SysUser.EMAIL.getName(), "邮箱"));
        table.addColumn(new Column(SysUser.CREATE_TIME.getName(), "创建时间"));

        editLink.setImageSrc("/images/table-edit.png");
        editLink.setTitle("Edit user details");
        editLink.setParameter("referrer", "/introduction/advanced-table.htm");

        deleteLink.setImageSrc("/images/table-delete.png");
        deleteLink.setTitle("Delete user record");
        deleteLink.setAttribute("onclick", "return window.confirm('Are you sure you want to delete this record?');");

        // 每行加入两个按钮
        Column column = new Column("Action", "操作");
        column.setTextAlign("center");
        AbstractLink[] links = new AbstractLink[] { editLink, deleteLink };
        column.setDecorator(new LinkDecorator(table, links, SysUser.ID_PK_COLUMN));
        column.setSortable(false);
        table.addColumn(column);

        table.setDataProvider(new DataProvider<SysUser>() {
            /**  */
            private static final long serialVersionUID = 1L;

            public List<SysUser> getData() {
                if (sysUserDao != null) {
                    return sysUserDao.getSysUser();
                } else {
                    return null;
                }

            }
        });

        table.getControlLink().setActionListener(new ActionListener() {
            /** */
            private static final long serialVersionUID = 1L;

            public boolean onAction(Control source) {
                // Save Table sort and paging state between requests.
                // NOTE: we set the listener on the table's Link control which is invoked
                // when the Link is clicked, such as when paging or sorting.
                // This ensures the table state is only saved when the state changes, and
                // cuts down on unnecessary session replication in a cluster environment.
                table.saveState(getContext());
                return true;
            }
        });

        // Restore the table sort and paging state from the session between requests
        table.restoreState(getContext());
    }

    /**
     *
     * @return
     */
    public boolean onDeleteClick() {
        Integer id = deleteLink.getValueInteger();
        sysUserDao.deleteSysUser(id);
        return true;
    }

}
