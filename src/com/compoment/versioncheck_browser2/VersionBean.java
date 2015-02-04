package com.compoment.versioncheck_browser2;

public class VersionBean{
	private String update;
	private String pkg_url;
	private String update_content;
	private String version;
	
	public VersionBean(String update, String pkg_url, String update_content,
			String version) {
		super();
		this.update = update;
		this.pkg_url = pkg_url;
		this.update_content = update_content;
		this.version = version;
	}
	public VersionBean(){
		
	}
	public String getUpdate() {
		return update;
	}
	public void setUpdate(String update) {
		this.update = update;
	}
	public String getPkg_url() {
		return pkg_url;
	}
	public void setPkg_url(String pkg_url) {
		this.pkg_url = pkg_url;
	}
	public String getUpdate_content() {
		return update_content;
	}
	public void setUpdate_content(String update_content) {
		this.update_content = update_content;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pkg_url == null) ? 0 : pkg_url.hashCode());
		result = prime * result + ((update == null) ? 0 : update.hashCode());
		result = prime * result
				+ ((update_content == null) ? 0 : update_content.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VersionBean other = (VersionBean) obj;
		if (pkg_url == null) {
			if (other.pkg_url != null)
				return false;
		} else if (!pkg_url.equals(other.pkg_url))
			return false;
		if (update == null) {
			if (other.update != null)
				return false;
		} else if (!update.equals(other.update))
			return false;
		if (update_content == null) {
			if (other.update_content != null)
				return false;
		} else if (!update_content.equals(other.update_content))
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "VersionBean [update=" + update + ", pkg_url=" + pkg_url
				+ ", update_content=" + update_content + ", version=" + version
				+ "]";
	}
	

}
