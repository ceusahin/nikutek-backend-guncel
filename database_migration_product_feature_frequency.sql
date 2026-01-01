-- Product Feature tablosuna frequency kolonu ekle
ALTER TABLE nikutek.product_feature
ADD COLUMN IF NOT EXISTS frequency INTEGER;

-- Mevcut kayıtlar için varsayılan değer (opsiyonel - null olabilir)
-- UPDATE nikutek.product_feature SET frequency = 50 WHERE frequency IS NULL;

