ALTER TABLE public.anuncio ADD catalog_listing BOOLEAN;
UPDATE anuncio set catalog_listing = false;
ALTER TABLE anuncio ALTER COLUMN catalog_listing SET NOT NULL;
