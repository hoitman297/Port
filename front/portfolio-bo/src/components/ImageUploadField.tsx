import { useRef, useState } from 'react';
import { ApiError, uploadImage } from '../lib/api';

export function ImageUploadField({
  label,
  value,
  onChange,
}: {
  label: string;
  value: string | null;
  onChange: (url: string | null) => void;
}) {
  const inputRef = useRef<HTMLInputElement>(null);
  const [uploading, setUploading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  async function handleFileChange(e: React.ChangeEvent<HTMLInputElement>) {
    const file = e.target.files?.[0];
    if (!file) return;

    setError(null);
    setUploading(true);
    try {
      const res = await uploadImage(file);
      onChange(res.imageUrl);
    } catch (err) {
      setError(err instanceof ApiError ? err.message : '이미지 업로드 중 오류가 발생했습니다.');
    } finally {
      setUploading(false);
      if (inputRef.current) inputRef.current.value = '';
    }
  }

  return (
    <div className="image-upload-field">
      <label>{label}</label>
      {value && (
        <div className="image-preview">
          <img src={value} alt="" />
          <button type="button" onClick={() => onChange(null)}>
            제거
          </button>
        </div>
      )}
      <input ref={inputRef} type="file" accept="image/*" onChange={handleFileChange} disabled={uploading} />
      {uploading && <p>업로드 중…</p>}
      {error && <p className="error-text">{error}</p>}
    </div>
  );
}
