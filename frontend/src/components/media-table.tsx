import { filesize } from "filesize";
import { Column } from "primereact/column";
import { DataTable } from 'primereact/datatable';
import { useEffect, useState } from "react";


interface IFileMetadata {
    id: string;
    name: string;
    absolutePath: string;
    size: number;
    creationTime: number;
    lastAccessTime: number;
    lastModifiedTime: number;
    extension: string;
    fileKey: string;
}

const MediaTable = () => {
    const [loading, setLoading] = useState<boolean>();
    const [files, setFiles] = useState<IFileMetadata[]>([]);

    useEffect(() => {
        loadFiles();
    }, [])

    const loadFiles = () => {
        setLoading(true);
        fetch('/api/media')
            .then(res => res.json())
            .then((data: IFileMetadata[]) => setFiles(data))
            .finally(() => setLoading(false));
    }

    const formatModifiedTime = (file: IFileMetadata) => {
        return <>{new Date(file.lastModifiedTime).toLocaleString()}</>
    };

    const formatSize = (file: IFileMetadata) => {
        return filesize(file.size);
    };

    const imageBodyTemplate = (file: IFileMetadata) => {
        const baseUrl = import.meta.env.VITE_API_BASE_URL;
        return <img src={`${baseUrl}/api/media/${file.id}/thumbnail`} alt={file.name} className="w-6rem shadow-2 border-round" />;
    };

    return (
        <div className='files'>
            <div className="card">
                <DataTable
                    value={files}
                    paginator
                    rows={50}
                    rowsPerPageOptions={[50, 100, 1000]}
                    tableStyle={{ minWidth: '50rem' }}
                    size="small"
                    loading={loading}
                >
                    <Column
                        header="Preview"
                        body={imageBodyTemplate}
                        style={{ width: '20%' }}
                    ></Column>
                    <Column
                        field="name"
                        header="Name"
                        style={{ width: '40%' }}
                        sortable
                    ></Column>
                    <Column
                        field="lastModifiedTime"
                        header="Modified"
                        body={formatModifiedTime}
                        style={{ width: '20%' }}
                        sortable
                    ></Column>
                    <Column
                        field="size"
                        header="Size"
                        body={formatSize}
                        style={{ width: '20%' }}
                        sortable
                    ></Column>
                </DataTable>
            </div>
        </div>
    );
}

export default MediaTable;